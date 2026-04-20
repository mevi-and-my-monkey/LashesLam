package com.mevi.lasheslam.ui.common

interface UiState<T> {
    val isLoading: Boolean
    fun copyWithLoading(isLoading: Boolean): T
}