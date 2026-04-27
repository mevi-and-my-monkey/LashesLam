package com.mevi.lasheslam.ui.courses

import com.mevi.lasheslam.network.CoursesItem
import com.mevi.lasheslam.ui.common.UiState
import com.mevi.lasheslam.ui.home.components.Section

data class CourseUiState(
    val courses: List<CoursesItem> = emptyList(),
    override val isLoading: Boolean = false,
    val selectedSection: Section = Section.CURSOS,
) : UiState<CourseUiState> {

    override fun copyWithLoading(isLoading: Boolean): CourseUiState {
        return copy(isLoading = isLoading)
    }
}