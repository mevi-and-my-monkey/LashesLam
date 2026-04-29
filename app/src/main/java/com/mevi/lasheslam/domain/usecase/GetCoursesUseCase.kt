package com.mevi.lasheslam.domain.usecase


import com.mevi.lasheslam.domain.repository.CoursesRepository
import javax.inject.Inject

class GetCoursesUseCase @Inject constructor(
    private val repo: CoursesRepository
) {
    operator fun invoke() = repo.getAllCourses()
}