package com.mevi.lasheslam.network

data class RequestCurseItemModel(
    val requestId: String = "",
    val userId: String = "",
    val courseId: String = "",
    val status: String = "pending",
    val timestamp: Long = System.currentTimeMillis()
)