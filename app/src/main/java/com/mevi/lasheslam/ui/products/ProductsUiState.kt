package com.mevi.lasheslam.ui.products

import com.mevi.lasheslam.domain.model.CategoryModel
import com.mevi.lasheslam.domain.model.ProductDetail
import com.mevi.lasheslam.ui.common.UiState

data class ProductsUiState(
    override val isLoading: Boolean = false,
    val isAdmin: Boolean = false,
    val isUserInvited: Boolean = false,
    val currentUserId: String? = null,
    val nameUser: String? = null,
    val email: String? = null,
    val facebook: String? = null,
    val instagram: String? = null,
    val whatsApp: String? = null,
    val productDetail: ProductDetail = ProductDetail(),
    val form: ProductsFormState = ProductsFormState(),
    val categoriesProducts: List<CategoryModel> = emptyList(),
    ) : UiState<ProductsUiState> {
    override fun copyWithLoading(isLoading: Boolean): ProductsUiState {
        return copy(isLoading = isLoading)
    }
}
