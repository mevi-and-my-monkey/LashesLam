package com.mevi.lasheslam.ui.services

import com.mevi.lasheslam.ui.common.UiState

data class ServiceUiState(
    override val isLoading: Boolean = false,
    val form: ServicesFormState = ServicesFormState(),
    ) : UiState<ServiceUiState> {
    override fun copyWithLoading(isLoading: Boolean): ServiceUiState {
        return copy(isLoading = isLoading)
    }

}