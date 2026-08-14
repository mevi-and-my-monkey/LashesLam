package com.mevi.lasheslam.domain.model

data class UserModel(
    val name: String? = null,
    val email: String? = null,
    val uid: String? = null,
    val phone: String? = null,
    val address: String? = null,
    val cartItems: Map<String, Long> = emptyMap(),
    val userPhoto: String? = null,
    val photoUpdatedByUser: Boolean = false,
    val password: String? = null,
    val confirmPassword: String? = null
)