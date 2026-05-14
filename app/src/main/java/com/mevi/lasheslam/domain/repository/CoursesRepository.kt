package com.mevi.lasheslam.domain.repository

import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.model.CreateCourseModel
import com.mevi.lasheslam.network.CoursesItem
import com.mevi.lasheslam.network.CreateCourseDto
import kotlinx.coroutines.flow.Flow

interface CoursesRepository {
    suspend fun createCourse(course: CreateCourseModel): Resource<Unit>
    suspend fun getCoursesByIds(ids: List<String>): Resource<List<CoursesItem>>
    fun getAllCourses(): Flow<Resource<List<CoursesItem>>>
    suspend fun getCourseById(courseId: String): Resource<CreateCourseDto>
}