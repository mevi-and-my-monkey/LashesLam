package com.mevi.lasheslam.domain.model

data class CreateCourseRequestModel(
    val userId: String,
    val userName: String,
    val userEmail: String,
    val courseId: String,
    val courseName: String,
    val date: String,
    val schedule: String,
    val price: String = "",
    val location: String = "",
    val apartar: String = ""
)