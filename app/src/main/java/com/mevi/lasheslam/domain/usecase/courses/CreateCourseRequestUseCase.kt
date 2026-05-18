package com.mevi.lasheslam.domain.usecase.courses

import com.mevi.lasheslam.core.di.IoDispatcher
import com.mevi.lasheslam.domain.repository.CoursesRepository
import com.mevi.lasheslam.domain.model.CreateCourseRequestModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CreateCourseRequestUseCase @Inject constructor(
    private val repository: CoursesRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(request: CreateCourseRequestModel) = withContext(dispatcher) {
        repository.createCourseRequest(request)
        }
    }