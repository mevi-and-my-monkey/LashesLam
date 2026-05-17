package com.mevi.lasheslam.domain.usecase.courses

import com.mevi.lasheslam.domain.repository.CoursesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCourseStatusUseCase @Inject constructor(
    private val repo: CoursesRepository
) {

    operator fun invoke(userId: String, courseId: String): Flow<String> {
        return repo.observeUserCourseStatus(userId, courseId)
    }
}