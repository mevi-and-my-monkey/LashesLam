package com.mevi.lasheslam.domain.model

data class RequestCurseItemModel(
    val requestId: String = "",
    val userId: String = "",
    val courseId: String = "",
    val status: String = "pending",
    val timestamp: Long = System.currentTimeMillis()
)