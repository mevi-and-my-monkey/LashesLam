package com.mevi.lasheslam.ui.products.search

import com.mevi.lasheslam.core.error.AppError


sealed class SearchUiEvent {
    data class ShowError(val error: AppError) : SearchUiEvent()
    object ShowComingSoon : SearchUiEvent()
}