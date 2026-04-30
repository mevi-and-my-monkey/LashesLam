package com.mevi.lasheslam.domain.model

data class SessionData(
    val isAdmin: Boolean,
    val isInvited: Boolean,
    val userId: String,
    val nameUser: String,
    val photoUser: String
)
