package com.mevi.lasheslam.ui.products

import com.mevi.lasheslam.core.error.AppError

sealed class ProductUiEvent {
    object ProductSaved : ProductUiEvent()
    object ProductDeleted : ProductUiEvent()
    object ProductUpdated : ProductUiEvent()
    data class ShowError(val error: AppError) : ProductUiEvent()
}