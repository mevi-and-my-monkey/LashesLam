package com.mevi.lasheslam.ui.courses

import com.mevi.lasheslam.core.error.AppError

sealed class CoursesUiEvent {
    data class ShowError(val error: AppError) : CoursesUiEvent()
}