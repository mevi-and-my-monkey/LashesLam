package com.mevi.lasheslam.ui.home

import com.mevi.lasheslam.network.CoursesItem
import com.mevi.lasheslam.ui.common.UiState
import com.mevi.lasheslam.ui.home.components.Section

data class HomePageUiState (
    val courses: List<CoursesItem> = emptyList(),
    override val isLoading: Boolean = false,
    val selectedSection: Section = Section.CURSOS,
    val isAdmin: Boolean = false
) : UiState<HomePageUiState> {

    override fun copyWithLoading(isLoading: Boolean): HomePageUiState {
        return copy(isLoading = isLoading)
    }
}