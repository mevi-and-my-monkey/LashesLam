package com.mevi.lasheslam.data

import com.mevi.lasheslam.domain.repository.SessionDataSource
import com.mevi.lasheslam.session.SessionManager
import javax.inject.Inject

class SessionDataSourceImpl @Inject constructor() : SessionDataSource {

    override val isUserAdmin = SessionManager.isUserAdmin

    override suspend fun refreshAdmins() {
        SessionManager.refreshAdmins()
    }

    override fun isAdmin(email: String): Boolean {
        return SessionManager.isAdmin(email)
    }

    override fun setAdmin(value: Boolean) {
        SessionManager.setAdmin(value)
    }

    override fun setInvited(value: Boolean) {
        SessionManager.setInvited(value)
    }

    override fun setCurrentUserId(uid: String?) {
        SessionManager.setCurrentUserId(uid)
    }

    override fun setEmailUser(email: String?) {
        SessionManager.setEmailUser(email)
    }
}