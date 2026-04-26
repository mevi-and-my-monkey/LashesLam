package com.mevi.lasheslam.data

import com.google.firebase.auth.FirebaseAuth
import com.mevi.lasheslam.domain.repository.SessionRepository
import com.mevi.lasheslam.session.SessionManager
import javax.inject.Inject

class SessionRepositoryImpl @Inject constructor(private val firebaseAuth: FirebaseAuth) :
    SessionRepository {
    override fun isLoggedIn(): Boolean = firebaseAuth.currentUser != null

    override fun getEmail(): String? = firebaseAuth.currentUser?.email

    override fun getUid(): String? = firebaseAuth.currentUser?.uid

    override suspend fun refreshSession() {
        SessionManager.refreshAdmins()
    }

    override fun isAdmin(email: String): Boolean = SessionManager.isAdmin(email)

    override fun setAdmin(isAdmin: Boolean) {
        SessionManager.setAdmin(isAdmin)
        SessionManager.setInvited(false)
    }

    override fun setSessionManager() {
        SessionManager.setCurrentUserId(getUid())
        SessionManager.setEmailUser(getEmail())
    }
}