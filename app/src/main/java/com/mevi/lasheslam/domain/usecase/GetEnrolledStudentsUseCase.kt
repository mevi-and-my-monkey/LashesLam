package com.mevi.lasheslam.domain.usecase

import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.repository.EnrolledRepository
import com.mevi.lasheslam.domain.model.EnrolledStudent
import javax.inject.Inject

class GetEnrolledStudentsUseCase @Inject constructor(
    private val repository: EnrolledRepository
) {
    suspend operator fun invoke(courseId: String): Resource<List<EnrolledStudent>> =
        repository.getStudents(courseId)
}
