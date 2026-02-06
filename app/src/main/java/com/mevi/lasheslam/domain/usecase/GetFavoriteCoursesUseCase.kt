package com.mevi.lasheslam.domain.usecase

import com.mevi.lasheslam.domain.repository.CoursesRepository
import javax.inject.Inject

class GetFavoriteCoursesUseCase @Inject constructor(
    private val repo: CoursesRepository
) {
    suspend operator fun invoke(ids: List<String>) =
        repo.getCoursesByIds(ids)
}