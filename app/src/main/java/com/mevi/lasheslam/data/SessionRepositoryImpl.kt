package com.mevi.lasheslam.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mevi.lasheslam.data.constants.FirestorePaths
import com.mevi.lasheslam.domain.repository.SessionDataSource
import com.mevi.lasheslam.domain.repository.SessionRepository
import com.mevi.lasheslam.network.LocationItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SessionRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val sessionDataSource: SessionDataSource
) : SessionRepository {
    override fun isLoggedIn(): Boolean = firebaseAuth.currentUser != null

    override fun getEmail(): String? = firebaseAuth.currentUser?.email

    override fun getUid(): String? = firebaseAuth.currentUser?.uid

    override suspend fun setName() {

        val uid = getUid() ?: return

        val snapshot = firestore.collection(FirestorePaths.Users.COLLECTION)
            .document(uid)
            .get()
            .await()

        val name = snapshot.getString(FirestorePaths.Users.USER_NAME)
            ?.split(" ")
            ?.firstOrNull()
            .orEmpty()

        sessionDataSource.setNameUser(name)
    }

    override suspend fun setPhoto() {
        val firebasePhoto = firebaseAuth.currentUser?.photoUrl?.toString()

        if (firebasePhoto.isNullOrEmpty()) return

        sessionDataSource.setPhotoUrl(firebasePhoto)
    }

    override fun getUserName(): Flow<String?> {
        return sessionDataSource.nameUser
    }

    override fun getPhotoUrl(): Flow<String?> {
        return sessionDataSource.photoUrl
    }

    override fun getFlowEmail(): Flow<String?> {
        return sessionDataSource.email
    }

    override fun getFacebook(): Flow<String?> {
        return sessionDataSource.facebook
    }

    override fun getInstagram(): Flow<String?> {
        return sessionDataSource.instagram
    }

    override fun getWhatsApp(): Flow<String?> {
        return sessionDataSource.whatsApp
    }


    override suspend fun refreshSession() {
        sessionDataSource.refreshAdmins()
    }

    override fun isAdmin(email: String): Boolean {
        return sessionDataSource.isAdmin(email)
    }

    override fun setAdmin(isAdmin: Boolean) {
        sessionDataSource.setAdmin(isAdmin)
        sessionDataSource.setInvited(false)
    }

    override fun setSessionManager() {
        sessionDataSource.setCurrentUserId(getUid())
        sessionDataSource.setEmailUser(getEmail())
    }

    override fun getIsAdmin(): Flow<Boolean> {
        return sessionDataSource.isUserAdmin
    }

    override fun getIsUserInvited(): Flow<Boolean> {
        return sessionDataSource.isUserInvited
    }

    override fun getCurrentUserId(): Flow<String?> {
        return sessionDataSource.currentUserId
    }

    override fun getLocations(): Flow<List<LocationItem>> {
        return sessionDataSource.locations
    }


}