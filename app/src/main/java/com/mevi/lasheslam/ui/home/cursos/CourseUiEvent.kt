package com.mevi.lasheslam.ui.home.cursos

import com.mevi.lasheslam.core.error.AppError

sealed class CourseUiEvent {
    object CourseSaved : CourseUiEvent()
    object CourseUpdated : CourseUiEvent()
    object CourseDeleted : CourseUiEvent()
    object RequestCourse : CourseUiEvent()
    data class ShowError(val error: AppError) : CourseUiEvent()
}