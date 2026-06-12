package com.mevi.lasheslam.data

import android.net.Uri
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.mevi.lasheslam.core.error.AppError
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.core.error.ErrorMapper
import com.mevi.lasheslam.data.constants.FirestoreOptions
import com.mevi.lasheslam.data.constants.FirestorePaths
import com.mevi.lasheslam.data.constants.StoragePaths
import com.mevi.lasheslam.data.dto.UserDto
import com.mevi.lasheslam.data.mappers.toDto
import com.mevi.lasheslam.domain.repository.UserRepository
import com.mevi.lasheslam.network.UserModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class UserRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
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
                val firebaseUser = authResult.user ?: return@withContext Resource.Error(AppError.Unknown(null))

                val userId = firebaseUser.uid

                // Se guarda como UserDto para no persistir password/confirmPassword
                val userToSave = user.copy(uid = userId).toDto()
                firestore.document(FirestorePaths.Users.document(userId))
                    .set(userToSave, FirestoreOptions.MERGE).await()

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

                val userDoc = firestore.document(FirestorePaths.Users.document(userId))
                val snapshot = userDoc.get().await()

                if (!snapshot.exists()) {
                    val dto = UserDto(
                        name = user.displayName,
                        email = user.email,
                        uid = user.uid,
                        phone = user.phoneNumber,
                        address = "",
                        userPhoto = user.photoUrl?.toString(),
                        photoUpdatedByUser = false
                    )
                    userDoc.set(dto, FirestoreOptions.MERGE).await()
                } else {
                    // Usuario existente: la foto la sincroniza setPhoto() respetando
                    // photoUpdatedByUser; no se sobrescriben address ni phone guardados
                    val identity = mapOf(
                        FirestorePaths.Users.USER_NAME to user.displayName,
                        "email" to user.email,
                        "uid" to user.uid
                    ).filterValues { it != null }
                    userDoc.set(identity, FirestoreOptions.MERGE).await()
                }

                Resource.Success(true)
            } catch (e: Exception) {
                Resource.Error(errorMapper.map(e))
            }
        }

    override suspend fun updateProfilePhoto(imageUri: Uri): Resource<String> =
        withContext(Dispatchers.IO) {
            try {
                val userId = auth.currentUser?.uid
                    ?: return@withContext Resource.Error(AppError.Unknown(null))

                val reference = storage.reference.child(StoragePaths.Users.profilePhoto(userId))
                reference.putFile(imageUri).await()
                val photoUrl = reference.downloadUrl.await().toString()

                firestore.document(FirestorePaths.Users.document(userId))
                    .set(
                        mapOf(
                            FirestorePaths.Users.USER_PHOTO to photoUrl,
                            FirestorePaths.Users.PHOTO_UPDATED_BY_USER to true
                        ),
                        FirestoreOptions.MERGE
                    ).await()

                Resource.Success(photoUrl)
            } catch (e: Exception) {
                Resource.Error(errorMapper.map(e))
            }
        }
}