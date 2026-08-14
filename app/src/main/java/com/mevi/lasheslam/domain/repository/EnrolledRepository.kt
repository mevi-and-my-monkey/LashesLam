package com.mevi.lasheslam.domain.repository

import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.model.EnrolledCourse
import com.mevi.lasheslam.domain.model.EnrolledStudent

interface EnrolledRepository {
    suspend fun getCourses(): Resource<List<EnrolledCourse>>
    suspend fun getStudents(courseId: String): Resource<List<EnrolledStudent>>
}
