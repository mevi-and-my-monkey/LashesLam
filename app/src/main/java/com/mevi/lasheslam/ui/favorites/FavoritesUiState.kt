package com.mevi.lasheslam.ui.favorites

import com.mevi.lasheslam.network.CoursesItem
import com.mevi.lasheslam.network.ProductItem
import com.mevi.lasheslam.network.ServiceItem
import com.mevi.lasheslam.ui.common.UiState
import com.mevi.lasheslam.ui.home.components.Section


data class FavoritesUiState(
    override val isLoading: Boolean = false,
    val selectedSection: Section = Section.CURSOS,
    val isAdmin: Boolean = false,
    val isUserInvited: Boolean = false,
    val currentUserId: String? = null,
    val nameUser: String? = null,
    val photoUser: String? = null,
    val email: String? = null,
    val facebook: String? = null,
    val instagram: String? = null,
    val whatsApp: String? = null,
    val favoriteCourses: List<CoursesItem> = emptyList(),
    val favoriteProducts: List<ProductItem> = emptyList(),
    val favoriteServices: List<ServiceItem> = emptyList()
) : UiState<FavoritesUiState> {
    override fun copyWithLoading(isLoading: Boolean): FavoritesUiState {
        return copy(isLoading = isLoading)
    }
}
