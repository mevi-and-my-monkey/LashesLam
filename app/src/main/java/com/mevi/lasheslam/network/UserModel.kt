package com.mevi.lasheslam.network

data class UserModel(
    val name: String? = null,
    val email: String? = null,
    val uid: String? = null,
    val phone: String? = null,
    val address: String? = null,
    val cartItems: Map<String, Long> = emptyMap(),
)