package com.mevi.lasheslam.domain.notifications

import com.mevi.lasheslam.domain.repository.UserCourseRepository
import javax.inject.Inject

class ObserveUserCoursesUseCase @Inject constructor(
    private val repository: UserCourseRepository
) {
    operator fun invoke(userId: String) =
        repository.observeAcceptedCourses(userId)
}