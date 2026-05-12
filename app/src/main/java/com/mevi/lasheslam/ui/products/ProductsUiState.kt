package com.mevi.lasheslam.ui.products

import com.mevi.lasheslam.network.CategoryModel
import com.mevi.lasheslam.ui.common.UiState

data class ProductsUiState(
    override val isLoading: Boolean = false,
    val form: ProductsFormState = ProductsFormState(),
    val categoriesProducts: List<CategoryModel> = emptyList(),
    ) : UiState<ProductsUiState> {
    override fun copyWithLoading(isLoading: Boolean): ProductsUiState {
        return copy(isLoading = isLoading)
    }
}
