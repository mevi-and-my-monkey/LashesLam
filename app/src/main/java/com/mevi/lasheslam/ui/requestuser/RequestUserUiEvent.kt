package com.mevi.lasheslam.ui.requestuser

import com.mevi.lasheslam.core.error.AppError

sealed class RequestUserUiEvent {
    object ProductSaved : RequestUserUiEvent()
    data class ShowError(val error: AppError) : RequestUserUiEvent()
}