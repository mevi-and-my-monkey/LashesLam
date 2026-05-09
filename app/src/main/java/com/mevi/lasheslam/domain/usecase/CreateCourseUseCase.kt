package com.mevi.lasheslam.domain.usecase

import com.mevi.lasheslam.core.error.AppError
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.model.CreateCourseModel
import com.mevi.lasheslam.domain.repository.CoursesRepository
import com.mevi.lasheslam.domain.validation.CourseValidationMessages
import javax.inject.Inject

class CreateCourseUseCase @Inject constructor(private val repository: CoursesRepository) {
    suspend operator fun invoke(course: CreateCourseModel): Resource<Unit> {
        validateRequired(course.titulo, CourseValidationMessages.TITLE_REQUIRED)?.let { return it }
        validateRequired(
            course.descripcion,
            CourseValidationMessages.DESCRIPTION_REQUIRED
        )?.let { return it }
        validateRequired(
            course.horaInicio,
            CourseValidationMessages.START_HOUR_REQUIRED
        )?.let { return it }
        validateRequired(
            course.horaFin,
            CourseValidationMessages.END_HOUR_REQUIRED
        )?.let { return it }
        validateRequired(course.fecha, CourseValidationMessages.DATE_REQUIRED)?.let { return it }
        validateRequired(course.costo, CourseValidationMessages.COST_REQUIRED)?.let { return it }
        validateRequired(
            course.apartado,
            CourseValidationMessages.APARTADO_REQUIRED
        )?.let { return it }
        validateRequired(
            course.instructora,
            CourseValidationMessages.INSTRUCTOR_REQUIRED
        )?.let { return it }
        validateRequired(
            course.instructoraDesc,
            CourseValidationMessages.INSTRUCTOR_DESC_REQUIRED
        )?.let { return it }
        validateRequired(
            course.imageUri.toString(),
            CourseValidationMessages.COURSE_IMAGE_REQUIRED
        )?.let { return it }
        validateRequired(
            course.instructorImageUri.toString(),
            CourseValidationMessages.INSTRUCTOR_IMAGE_REQUIRED
        )?.let { return it }
        validateRequired(
            course.temarios.joinToString(),
            CourseValidationMessages.TEMARIOS_REQUIRED
        )?.let { return it }
        validateRequired(
            course.ubicacionNombre.toString(),
            CourseValidationMessages.LOCATION_REQUIRED
        )?.let { return it }
        validateRequired(
            course.lat.toString(),
            CourseValidationMessages.INVALID_LOCATION
        )?.let { return it }
        validateRequired(
            course.lng.toString(),
            CourseValidationMessages.INVALID_LOCATION
        )?.let { return it }
        return repository.createCourse(course)
    }

    private fun validateRequired(
        value: String,
        message: String
    ): Resource.Error? {

        return if (value.isBlank()) {
            Resource.Error(AppError.Unknown(message))
        } else {
            null
        }
    }
}