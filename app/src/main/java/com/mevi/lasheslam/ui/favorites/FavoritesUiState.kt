package com.mevi.lasheslam.ui.favorites

import com.mevi.lasheslam.ui.common.UiState


data class FavoritesUiState(
    override val isLoading: Boolean = false,
    val isAdmin: Boolean = false,
    val isUserInvited: Boolean = false,
    val currentUserId: String? = null,
    val nameUser: String? = null,
    val email: String? = null,
    val facebook: String? = null,
    val instagram: String? = null,
    val whatsApp: String? = null,
) : UiState<FavoritesUiState> {
    override fun copyWithLoading(isLoading: Boolean): FavoritesUiState {
        return copy(isLoading = isLoading)
    }
}
