package com.mevi.lasheslam.domain.model

data class EnrolledStudent(
    val requestId: String = "",
    val userId: String = "",
    val nameUser: String = "",
    val emailUser: String = "",
    val courseId: String = "",
    val courseName: String = "",
    val status: String = "",
    val date: String = "",
    val schedule: String = "",
    val userPhoto: String = "",
    val timestamp: Long = 0L
)
