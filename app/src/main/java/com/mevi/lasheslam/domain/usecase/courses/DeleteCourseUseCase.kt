package com.mevi.lasheslam.domain.usecase.courses

import com.mevi.lasheslam.domain.repository.CoursesRepository
import javax.inject.Inject

class DeleteCourseUseCase @Inject constructor(private val repository: CoursesRepository) {
    suspend operator fun invoke(courseId: String, imageUrl: String) =
        repository.deleteCourse(courseId = courseId, imageUrl = imageUrl)
}