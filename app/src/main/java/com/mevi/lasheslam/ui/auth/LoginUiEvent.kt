package com.mevi.lasheslam.ui.auth

sealed class LoginUiEvent {
    object NavigateToHome : LoginUiEvent()
    object RegisterSuccess : LoginUiEvent()
    data class ShowError(val message: String) : LoginUiEvent()
}