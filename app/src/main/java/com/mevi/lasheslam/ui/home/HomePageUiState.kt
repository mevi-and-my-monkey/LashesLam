package com.mevi.lasheslam.ui.home

import com.mevi.lasheslam.network.CategoryModel
import com.mevi.lasheslam.network.CoursesItem
import com.mevi.lasheslam.network.ProductItem
import com.mevi.lasheslam.network.ServiceItem
import com.mevi.lasheslam.ui.common.UiState
import com.mevi.lasheslam.ui.home.components.Section

data class HomePageUiState(
    override val isLoading: Boolean = false,
    val courses: List<CoursesItem> = emptyList(),
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
    val whatsApp: String? = null,
    val cartCount: Int = 0,
) : UiState<HomePageUiState> {

    override fun copyWithLoading(isLoading: Boolean): HomePageUiState {
        return copy(isLoading = isLoading)
    }
}