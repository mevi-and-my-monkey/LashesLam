package com.mevi.lasheslam.ui.home.cursos

import com.mevi.lasheslam.core.error.AppError

sealed class CourseUiEvent {
    object CourseSaved : CourseUiEvent()
    data class ShowError(val error: AppError) : CourseUiEvent()
}