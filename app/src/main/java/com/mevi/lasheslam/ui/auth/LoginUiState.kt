package com.mevi.lasheslam.ui.auth

import com.mevi.lasheslam.ui.common.UiState

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoginEnabled: Boolean = false,
    override val isLoading: Boolean = false
) : UiState<LoginUiState> {

    override fun copyWithLoading(isLoading: Boolean): LoginUiState {
        return copy(isLoading = isLoading)
    }
}