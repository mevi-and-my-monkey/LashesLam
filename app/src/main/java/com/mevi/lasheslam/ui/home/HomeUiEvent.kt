package com.mevi.lasheslam.ui.home

import com.mevi.lasheslam.core.error.AppError

sealed class HomeUiEvent {
    data class ShowError(val error: AppError) : HomeUiEvent()
    object ShowComingSoon : HomeUiEvent()
}