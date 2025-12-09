package com.mevi.lasheslam.network

data class CourseRequest(
    val requestId: String = "",
    val userId: String = "",
    val courseId: String = "",
    val courseName: String = "",
    val status: String = "pendiente",
    val timestamp: Long = System.currentTimeMillis()
)
