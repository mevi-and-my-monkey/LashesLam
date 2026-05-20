package com.mevi.lasheslam.ui.home.cursos

import com.mevi.lasheslam.network.CreateCourseDto
import com.mevi.lasheslam.network.LocationItem
import com.mevi.lasheslam.network.UpdateCourseDto
import com.mevi.lasheslam.ui.courses.CourseFormState
import com.mevi.lasheslam.ui.common.UiState
import com.mevi.lasheslam.utils.Constants

data class CourseUiState(
    override val isLoading: Boolean = false,
    val isAdmin: Boolean = false,
    val isUserInvited: Boolean = false,
    val currentUserId: String? = null,
    val nameUser: String? = null,
    val email: String? = null,
    val facebook: String? = null,
    val instagram: String? = null,
    val whatsApp: String? = null,
    val form: CourseFormState = CourseFormState(),
    val courseDetail: CreateCourseDto = CreateCourseDto(),
    val courseUpdate: UpdateCourseDto = UpdateCourseDto(),
    val courseStatus: String = Constants.Course.STATUS_REQUESTED,
    val showSuccess: Boolean = false,
    val successMessage: String = "",
    val locations: List<LocationItem> = emptyList()
) : UiState<CourseUiState> {
    override fun copyWithLoading(isLoading: Boolean): CourseUiState {
        return copy(isLoading = isLoading)
    }
}
