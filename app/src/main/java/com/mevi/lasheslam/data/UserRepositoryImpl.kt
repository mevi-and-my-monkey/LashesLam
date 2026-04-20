package com.mevi.lasheslam.data

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.mevi.lasheslam.core.error.AppError
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.core.error.ErrorMapper
import com.mevi.lasheslam.domain.repository.UserRepository
import com.mevi.lasheslam.network.UserModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class UserRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val errorMapper: ErrorMapper
) : UserRepository {

    override suspend fun signIn(email: String, password: String): Resource<Boolean> =
        withContext(Dispatchers.IO) {
            try {
                auth.signInWithEmailAndPassword(email, password).await()
                Resource.Success(true)
            } catch (e: Exception) {
                Resource.Error(errorMapper.map(e))
            }
        }

    override suspend fun register(user: UserModel): Resource<Boolean> =
        withContext(Dispatchers.IO) {
            try {
                val authResult =
                    auth.createUserWithEmailAndPassword(user.email ?: "", user.password ?: "")
                        .await()
                val firebaseUser =
                    authResult.user
                        ?: return@withContext Resource.Error(AppError.Unknown(null))
                val userId = firebaseUser.uid

                val userToSave = user.copy(uid = userId)
                firestore.collection("users")
                    .document(userId)
                    .set(userToSave, SetOptions.merge()).await()

                Resource.Success(true)
            } catch (e: Exception) {
                Resource.Error(errorMapper.map(e))
            }
        }

    override suspend fun signInWithGoogle(credential: AuthCredential): Resource<Boolean> =
        withContext(Dispatchers.IO) {
            try {
                val authResult = auth.signInWithCredential(credential).await()

                val user = authResult.user
                val userId = user?.uid ?: return@withContext Resource.Error(AppError.Unknown(null))

                val userModel = UserModel(
                    name = user.displayName ?: "Sin nombre registrado",
                    email = user.email ?: "Sin correo electronico registrado",
                    uid = userId,
                    phone = user.phoneNumber ?: "Sin numero telefonico registrado",
                    address = "Sin direccion registrada",
                    userPhoto = user.photoUrl?.toString() ?: ""
                )
                firestore.collection("users")
                    .document(user.uid)
                    .set(userModel, SetOptions.merge())
                    .await()

                Resource.Success(true)
            } catch (e: Exception) {
                Resource.Error(errorMapper.map(e))
            }
        }
}