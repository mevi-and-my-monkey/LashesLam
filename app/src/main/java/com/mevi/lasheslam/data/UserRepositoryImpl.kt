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
            firestore.collection("users")
                .document(user.uid ?: "")
                .set(user)
                .await()
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error al registrar usuario")
        }
    }

    override suspend fun signInWithGoogle(credential: AuthCredential): Resource<Boolean> {
        return try {
            auth.signInWithCredential(credential).await()
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error con Google Sign-In")
        }
    }
}