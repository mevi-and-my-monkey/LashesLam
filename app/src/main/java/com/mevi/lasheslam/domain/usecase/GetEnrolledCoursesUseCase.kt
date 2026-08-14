package com.mevi.lasheslam.domain.usecase

import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.repository.EnrolledRepository
import com.mevi.lasheslam.domain.model.EnrolledCourse
import javax.inject.Inject

class GetEnrolledCoursesUseCase @Inject constructor(
    private val repository: EnrolledRepository
) {
    suspend operator fun invoke(): Resource<List<EnrolledCourse>> = repository.getCourses()
}
