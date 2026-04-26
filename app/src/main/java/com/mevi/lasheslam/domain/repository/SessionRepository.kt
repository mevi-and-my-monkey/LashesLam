package com.mevi.lasheslam.domain.repository

interface SessionRepository {
    fun isLoggedIn(): Boolean
    fun getEmail(): String?
    fun getUid(): String?
    suspend fun refreshSession()
    fun isAdmin(email: String): Boolean
    fun setAdmin(isAdmin: Boolean)
    fun setSessionManager()
}