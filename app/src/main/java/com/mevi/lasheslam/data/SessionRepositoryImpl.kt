package com.mevi.lasheslam.data

import com.google.firebase.auth.FirebaseAuth
import com.mevi.lasheslam.domain.repository.SessionDataSource
import com.mevi.lasheslam.domain.repository.SessionRepository
import com.mevi.lasheslam.session.SessionManager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SessionRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val sessionDataSource: SessionDataSource
) : SessionRepository {
    override fun isLoggedIn(): Boolean = firebaseAuth.currentUser != null

    override fun getEmail(): String? = firebaseAuth.currentUser?.email

    override fun getUid(): String? = firebaseAuth.currentUser?.uid

    override suspend fun refreshSession() { sessionDataSource.refreshAdmins() }

    override fun isAdmin(email: String): Boolean { return sessionDataSource.isAdmin(email) }

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
}