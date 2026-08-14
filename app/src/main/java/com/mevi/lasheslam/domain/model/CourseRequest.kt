package com.mevi.lasheslam.domain.model

data class CourseRequest(
    val requestId: String = "",
    val userId: String = "",
    val courseId: String = "",
    val courseName: String = "",
    val status: String = "",
    val date: String = "",
    val schedule: String = "",
    val nameUser: String = "",
    val emailUser: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val price: String = "",
    val location: String = "",
    val apartar: String = ""
)
