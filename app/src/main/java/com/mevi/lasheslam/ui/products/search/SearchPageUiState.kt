package com.mevi.lasheslam.ui.products.search

import com.mevi.lasheslam.domain.model.CategoryModel
import com.mevi.lasheslam.domain.model.CoursesItem
import com.mevi.lasheslam.domain.model.ProductItem
import com.mevi.lasheslam.domain.model.ServiceItem
import com.mevi.lasheslam.ui.common.UiState
import com.mevi.lasheslam.ui.home.components.Section

data class SearchPageUiState(
    override val isLoading: Boolean = false,
    val query: String = "",
    val courses: List<CoursesItem> = emptyList(),
    val filteredCourses: List<CoursesItem> = emptyList(),
    val products: List<ProductItem> = emptyList(),
    val services: List<ServiceItem> = emptyList(),
    val filteredProducts: List<ProductItem> = emptyList(),
    val filteredServices: List<ServiceItem> = emptyList(),
    val bestSellingProducts: List<ProductItem> = emptyList(),
    val categoriesProducts: List<CategoryModel> = emptyList(),
    val categoriesServices: List<CategoryModel> = emptyList(),
    val selectedSection: Section = Section.CURSOS,
    val isAdmin: Boolean = false,
    val isUserInvited: Boolean = false,
    val currentUserId: String? = null,
    val nameUser: String? = null,
    val photoUser: String? = null,
    val isProfileLoading: Boolean = true,
    val adminPendingCount: Int = 0,
    val facebook: String? = null,
    val instagram: String? = null,
    val whatsApp: String? = null
) : UiState<SearchPageUiState> {
    override fun copyWithLoading(isLoading: Boolean): SearchPageUiState {
        return copy(isLoading = isLoading)
    }
}