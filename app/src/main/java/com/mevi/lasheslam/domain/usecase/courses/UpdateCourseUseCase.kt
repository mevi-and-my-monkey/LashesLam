package com.mevi.lasheslam.domain.usecase.courses

import com.mevi.lasheslam.domain.model.UpdateCourseModel
import com.mevi.lasheslam.domain.repository.CoursesRepository
import javax.inject.Inject

class UpdateCourseUseCase @Inject constructor(
    private val repository: CoursesRepository, ) {
    suspend operator fun invoke(course: UpdateCourseModel)  {
        repository.updateCourse(course)
    }
}