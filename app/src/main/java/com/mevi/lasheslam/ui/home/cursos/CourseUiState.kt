package com.mevi.lasheslam.ui.home.cursos

import com.mevi.lasheslam.network.LocationItem
import com.mevi.lasheslam.ui.courses.CourseFormState
import com.mevi.lasheslam.ui.common.UiState

data class CourseUiState(
    override val isLoading: Boolean = false,
    val form: CourseFormState = CourseFormState(),
    val showSuccess: Boolean = false,
    val successMessage: String = "",
    val locations: List<LocationItem> = emptyList()
) : UiState<CourseUiState> {
    override fun copyWithLoading(isLoading: Boolean): CourseUiState {
        return copy(isLoading = isLoading)
    }
}
