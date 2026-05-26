package com.mevi.lasheslam.ui.requestuser

import com.mevi.lasheslam.network.CourseRequest
import com.mevi.lasheslam.ui.common.UiState
import com.mevi.lasheslam.ui.home.components.Section

data class RequestUserUiState(
    override val isLoading: Boolean = false,
    val selectedSection: Section = Section.CURSOS,
    val isAdmin: Boolean = false,
    val isUserInvited: Boolean = false,
    val currentUserId: String? = null,
    val nameUser: String? = null,
    val photoUser: String? = null,
    val email: String? = null,
    val facebook: String? = null,
    val instagram: String? = null,
    val whatsApp: String? = null,
    val requestUserCourses: List<CourseRequest> = emptyList()
) : UiState<RequestUserUiState> {
    override fun copyWithLoading(isLoading: Boolean): RequestUserUiState {
        return copy(isLoading = isLoading)
    }
}