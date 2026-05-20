package com.mevi.lasheslam.domain.usecase.courses

import com.mevi.lasheslam.core.di.IoDispatcher
import com.mevi.lasheslam.domain.model.UpdateCourseModel
import com.mevi.lasheslam.domain.repository.CoursesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateCourseUseCase @Inject constructor(
    private val repository: CoursesRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(course: UpdateCourseModel) = withContext(dispatcher) {
        repository.updateCourse(course)
    }
}