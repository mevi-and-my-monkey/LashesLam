package com.mevi.lasheslam.network

import com.mevi.lasheslam.data.constants.FirestorePaths

data class CourseRequest(
    val requestId: String = "",
    val userId: String = "",
    val courseId: String = "",
    val courseName: String = "",
    val status: String = FirestorePaths.Courses.STATUS_PANDING,
    val date: String = "",
    val schedule: String = "",
    val nameUser: String = "",
    val emailUser: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
