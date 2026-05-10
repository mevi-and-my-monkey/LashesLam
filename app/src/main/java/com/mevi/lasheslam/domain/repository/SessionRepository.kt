package com.mevi.lasheslam.domain.repository

import com.mevi.lasheslam.network.LocationItem
import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    suspend fun refreshSession()
    fun isLoggedIn(): Boolean
    fun getEmail(): String?
    fun getUid(): String?
    fun getPhotoUrl(): Flow<String?>
    fun setName()
    fun getUserName(): Flow<String?>
    fun isAdmin(email: String): Boolean
    fun setAdmin(isAdmin: Boolean)
    fun setSessionManager()
    fun getIsAdmin(): Flow<Boolean>
    fun getIsUserInvited(): Flow<Boolean>
    fun getCurrentUserId(): Flow<String?>
    fun getLocations(): Flow<List<LocationItem>>
}