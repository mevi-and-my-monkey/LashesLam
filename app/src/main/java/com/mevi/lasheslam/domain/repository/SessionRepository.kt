package com.mevi.lasheslam.domain.repository

import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    suspend fun refreshSession()
    fun isLoggedIn(): Boolean
    fun getEmail(): String?
    fun getUid(): String?
    fun isAdmin(email: String): Boolean
    fun setAdmin(isAdmin: Boolean)
    fun setSessionManager()
    fun getIsAdmin(): Flow<Boolean>
}