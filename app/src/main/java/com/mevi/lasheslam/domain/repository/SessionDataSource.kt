package com.mevi.lasheslam.domain.repository

import com.mevi.lasheslam.domain.model.LocationItem
import kotlinx.coroutines.flow.StateFlow

interface SessionDataSource {
    val isUserAdmin: StateFlow<Boolean>
    val isUserInvited: StateFlow<Boolean>
    val currentUserId: StateFlow<String?>
    val nameUser: StateFlow<String?>
    val photoUrl: StateFlow<String?>
    val email: StateFlow<String?>
    val facebook: StateFlow<String?>
    val instagram: StateFlow<String?>
    val whatsApp: StateFlow<String?>
    val clabe: StateFlow<String?>
    val locations: StateFlow<List<LocationItem>>
    val shippingCost: StateFlow<Double>

    suspend fun refreshAdmins()
    fun isAdmin(email: String): Boolean
    fun setAdmin(value: Boolean)
    fun setInvited(value: Boolean)
    fun setCurrentUserId(uid: String?)
    fun setEmailUser(email: String?)
    fun setNameUser(nameUser: String?)
    fun setPhotoUrl(photoUrl: String?)
    fun clearUserSession()
}