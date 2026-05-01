package com.mevi.lasheslam.ui.home.products

import androidx.compose.runtime.Composable
import com.mevi.lasheslam.network.CategoryModel
import com.mevi.lasheslam.ui.home.products.components.CategoriesProductsView

@Composable
fun ProductsHPContent(
    categories: List<CategoryModel>,
    selectedCategoryId: String?,
    onCategorySelected: (CategoryModel) -> Unit
) {
    CategoriesProductsView(
        categories = categories,
        selectedCategoryId = selectedCategoryId,
        onCategorySelected = onCategorySelected
    )

}