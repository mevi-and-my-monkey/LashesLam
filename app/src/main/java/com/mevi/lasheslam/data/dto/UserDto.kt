package com.mevi.lasheslam.data.dto

data class UserDto(
    val name: String? = null,
    val email: String? = null,
    val uid: String? = null,
    val phone: String? = null,
    val address: String? = null,
    val userPhoto: String? = null,
    val photoUpdatedByUser: Boolean = false
)