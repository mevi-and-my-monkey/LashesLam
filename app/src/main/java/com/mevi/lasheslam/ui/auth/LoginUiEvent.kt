package com.mevi.lasheslam.ui.auth

import com.mevi.lasheslam.core.error.AppError

sealed class LoginUiEvent {
    object NavigateToHome : LoginUiEvent()
    object RegisterSuccess : LoginUiEvent()
    data class ShowError(val error: AppError) : LoginUiEvent()
}