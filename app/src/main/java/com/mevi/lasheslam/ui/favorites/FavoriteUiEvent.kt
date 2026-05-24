package com.mevi.lasheslam.ui.favorites

import com.mevi.lasheslam.core.error.AppError

sealed class FavoriteUiEvent {
    object ProductSaved : FavoriteUiEvent()
    data class ShowError(val error: AppError) : FavoriteUiEvent()
}