package com.mevi.lasheslam.domain.usecase.courses

import com.mevi.lasheslam.domain.repository.CoursesRepository
import javax.inject.Inject

class GetACourseDetailUseCase @Inject constructor(
    private val repo: CoursesRepository
) {
    suspend operator fun invoke(courseId: String) = repo.getCourseById(courseId = courseId)
}