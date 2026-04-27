package com.mevi.lasheslam.domain.repository

import kotlinx.coroutines.flow.Flow

interface SessionDataSource {
    val isUserAdmin: Flow<Boolean>

    suspend fun refreshAdmins()
    fun isAdmin(email: String): Boolean
    fun setAdmin(value: Boolean)
    fun setInvited(value: Boolean)
    fun setCurrentUserId(uid: String?)
    fun setEmailUser(email: String?)
}