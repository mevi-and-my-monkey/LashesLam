package com.mevi.lasheslam.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mevi.lasheslam.data.constants.FirestorePaths
import com.mevi.lasheslam.domain.repository.SessionDataSource
import com.mevi.lasheslam.domain.repository.SessionRepository
import com.mevi.lasheslam.network.LocationItem
import com.mevi.lasheslam.session.SessionManager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SessionRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val sessionDataSource: SessionDataSource
) : SessionRepository {
    override fun isLoggedIn(): Boolean = firebaseAuth.currentUser != null

    override fun getEmail(): String? = firebaseAuth.currentUser?.email

    override fun getUid(): String? = firebaseAuth.currentUser?.uid


    override fun setName() {
        firestore.collection(FirestorePaths.Users.COLLECTION).document(getUid() ?: "")
            .get()
            .addOnSuccessListener {
                sessionDataSource.setNameUser(
                    it.getString(FirestorePaths.Users.USER_NAME)?.split(" ")?.firstOrNull() ?: ""
                )
            }
    }

    override fun getUserName(): Flow<String?> {
        return sessionDataSource.nameUser
    }

    override fun getPhotoUrl(): Flow<String?> {
        return sessionDataSource.photoUrl
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