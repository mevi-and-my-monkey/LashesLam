package com.mevi.lasheslam.network

import com.mevi.lasheslam.data.constants.FirestorePaths
import com.mevi.lasheslam.domain.model.CreateCourseRequestModel

data class CreateCourseRequestDto(
    val requestId: String = "",
    val userId: String = "",
    val nameUser: String = "",
    val emailUser: String = "",
    val courseId: String = "",
    val courseName: String = "",
    val status: String = "",
    val date: String = "",
    val schedule: String = "",
    val timestamp: Long = 0L
)

fun CreateCourseRequestModel.toDto(requestId: String): CreateCourseRequestDto {
    return CreateCourseRequestDto(
        requestId = requestId,
        userId = userId,
        nameUser = userName,
        emailUser = userEmail,
        courseId = courseId,
        courseName = courseName,
        status = FirestorePaths.Courses.STATUS_PANDING,
        date = date,
        schedule = schedule,
        timestamp = System.currentTimeMillis()
    )
}