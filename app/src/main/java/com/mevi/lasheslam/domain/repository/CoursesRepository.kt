package com.mevi.lasheslam.domain.repository

import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.model.CreateCourseModel
import com.mevi.lasheslam.network.CoursesItem
import com.mevi.lasheslam.network.CreateCourseDto
import com.mevi.lasheslam.domain.model.CreateCourseRequestModel
import com.mevi.lasheslam.domain.model.UpdateCourseModel
import kotlinx.coroutines.flow.Flow

interface CoursesRepository {
    fun getAllCourses(): Flow<Resource<List<CoursesItem>>>
    fun observeUserCourseStatus(userId: String, courseId: String): Flow<String>
    suspend fun createCourse(course: CreateCourseModel): Resource<Unit>
    suspend fun getCoursesByIds(ids: List<String>): Resource<List<CoursesItem>>
    suspend fun getCourseById(courseId: String): Resource<CreateCourseDto>
    suspend fun updateCourse(course: UpdateCourseModel): Resource<Unit>
    suspend fun deleteCourse(courseId: String, imageUrl: String): Resource<Unit>
    suspend fun createCourseRequest(request: CreateCourseRequestModel): Resource<Unit>
}