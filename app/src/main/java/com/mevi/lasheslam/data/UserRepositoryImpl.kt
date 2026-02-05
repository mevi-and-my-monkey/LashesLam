package com.mevi.lasheslam.data

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.repository.UserRepository
import com.mevi.lasheslam.network.UserModel
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : UserRepository {

    override suspend fun signIn(email: String, password: String): Resource<Boolean> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error de autenticación")
        }
    }

    override suspend fun register(user: UserModel): Resource<Boolean> {
        return try {
            val authResult =
                auth.createUserWithEmailAndPassword(user.email ?: "", user.uid ?: "").await()
            val firebaseUser =
                authResult.user ?: return Resource.Error("No se pudo crear el usuario")
            val userId = firebaseUser.uid

            val userToSave = user.copy(uid = userId)
            firestore.collection("users")
                .document(userId)
                .set(userToSave)
                .await()

            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error al registrar usuario")
        }
    }

    override suspend fun signInWithGoogle(credential: AuthCredential): Resource<Boolean> {
        return try {
            val authResult = auth.signInWithCredential(credential).await()

            val user = authResult.user
            val userId = user?.uid ?: return Resource.Error("No se pudo obtener el ID del usuario")

            val userModel = UserModel(
                name = user.displayName ?: "Sin nombre registrado",
                email = user.email ?: "Sin correo electronico registrado",
                uid = userId,
                phone = user.phoneNumber ?: "Sin numero telefonico registrado",
                address = "Sin direccion registrada",
                userPhoto = user.photoUrl?.toString() ?: ""
            )

            val userDoc = firestore.collection("users").document(userId)
            val snapshot = userDoc.get().await()

            if (!snapshot.exists()) {
                userDoc.set(userModel).await()
            }
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error con Google Sign-In")
        }
    }
}