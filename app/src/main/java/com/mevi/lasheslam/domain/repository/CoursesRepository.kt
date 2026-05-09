package com.mevi.lasheslam.domain.repository

import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.model.CreateCourseModel
import com.mevi.lasheslam.network.CoursesItem
import kotlinx.coroutines.flow.Flow

interface CoursesRepository {
    suspend fun createCourse(course: CreateCourseModel): Resource<Unit>
    suspend fun getCoursesByIds(ids: List<String>): Resource<List<CoursesItem>>
    fun getAllCourses(): Flow<Resource<List<CoursesItem>>>
}