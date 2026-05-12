package com.mevi.lasheslam.ui.services

import com.mevi.lasheslam.network.CategoryModel
import com.mevi.lasheslam.ui.common.UiState

data class ServiceUiState(
    override val isLoading: Boolean = false,
    val form: ServicesFormState = ServicesFormState(),
    val categoriesServices: List<CategoryModel> = emptyList(),
    ) : UiState<ServiceUiState> {
    override fun copyWithLoading(isLoading: Boolean): ServiceUiState {
        return copy(isLoading = isLoading)
    }

}