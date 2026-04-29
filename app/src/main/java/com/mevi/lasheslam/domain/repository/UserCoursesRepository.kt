package com.mevi.lasheslam.domain.repository

import com.mevi.lasheslam.domain.model.UserCourse
import kotlinx.coroutines.flow.Flow

interface UserCourseRepository {
    fun observeAcceptedCourses(userId: String): Flow<List<UserCourse>>
    suspend fun markNotificationAsCreated(
        userId: String,
        courseId: String
    )
}