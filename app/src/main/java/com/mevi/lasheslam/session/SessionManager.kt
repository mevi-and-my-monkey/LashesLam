package com.mevi.lasheslam.session

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object SessionManager {
    private val _isUserAdmin = MutableStateFlow(false)
    val isUserAdmin = _isUserAdmin.asStateFlow()

    private val _isUserInvited = MutableStateFlow(false)
    val isUserInvited = _isUserInvited.asStateFlow()

    private val _whatsApp = MutableStateFlow("")
    val whatsApp = _whatsApp.asStateFlow()

    // funciones para actualizar
    fun setAdmin(value: Boolean) { _isUserAdmin.value = value }
    fun setInvited(value: Boolean) { _isUserInvited.value = value }
    fun setWhatsApp(value: String) { _whatsApp.value = value }
}