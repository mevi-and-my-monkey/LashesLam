package com.mevi.lasheslam.ui.services

import com.mevi.lasheslam.core.error.AppError

sealed class ServiceUiEvent {
    object ServiceSaved : ServiceUiEvent()
    data class ShowError(val error: AppError) : ServiceUiEvent()
}