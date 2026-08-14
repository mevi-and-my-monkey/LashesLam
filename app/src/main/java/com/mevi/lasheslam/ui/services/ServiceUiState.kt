package com.mevi.lasheslam.ui.services

import com.mevi.lasheslam.domain.model.CategoryModel
import com.mevi.lasheslam.domain.model.ServiceDetail
import com.mevi.lasheslam.ui.common.UiState

data class ServiceUiState(
    override val isLoading: Boolean = false,
    val form: ServicesFormState = ServicesFormState(),
    val serviceDetail: ServiceDetail = ServiceDetail(),
    val categoriesServices: List<CategoryModel> = emptyList(),
    val facebook: String? = null,
    val instagram: String? = null,
    val whatsApp: String? = null,
    ) : UiState<ServiceUiState> {
    override fun copyWithLoading(isLoading: Boolean): ServiceUiState {
        return copy(isLoading = isLoading)
    }

}