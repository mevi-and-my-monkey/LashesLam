package com.mevi.lasheslam.domain.repository

import kotlinx.coroutines.flow.Flow

interface SessionDataSource {
    val isUserAdmin: Flow<Boolean>
    val isUserInvited: Flow<Boolean>
    val currentUserId: Flow<String?>
    val nameUser: Flow<String?>
    val photoUrl: Flow<String?>

    suspend fun refreshAdmins()
    fun isAdmin(email: String): Boolean
    fun setAdmin(value: Boolean)
    fun setInvited(value: Boolean)
    fun setCurrentUserId(uid: String?)
    fun setEmailUser(email: String?)
    fun setNameUser(nameUser: String?)
    fun setPhotoUrl(photoUrl: String?)
}