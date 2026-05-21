package com.mevi.lasheslam.ui.courses.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.mevi.lasheslam.domain.analytics.AnalyticsEvent
import com.mevi.lasheslam.navigation.Screen
import com.mevi.lasheslam.ui.common.toUserMessage
import com.mevi.lasheslam.ui.components.ErrorDialog
import com.mevi.lasheslam.ui.components.SuccessDialog
import com.mevi.lasheslam.ui.components.WarningDialog
import com.mevi.lasheslam.ui.home.cursos.CourseUiEvent
import com.mevi.lasheslam.ui.home.cursos.CourseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseDetailView(
    courseId: String,
    viewModel: CourseViewModel = hiltViewModel(),
    onDismiss: () -> Unit,
    modifier: Modifier,
    onEditClick: (String) -> Unit,
    onOpenFacebook: (String) -> Unit,
    onOpenInstagram: (String) -> Unit,
    onOpenWhatsApp: (String) -> Unit,
    onAddToCalendar: (
        titulo: String,
        fecha: String,
        horaInicio: String,
        horaFin: String
    ) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()


    var showConfirmDelete by remember { mutableStateOf(false) }
    var showSuccess by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var successTitle by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }
    var warningMessage by remember { mutableStateOf("") }

    LaunchedEffect(courseId) {
        viewModel.trackScreen(Screen.CourseDetails.route)
        viewModel.loadCourseById(courseId)
    }
    LaunchedEffect(courseId, uiState.currentUserId) {
        if (uiState.currentUserId != null) {
            viewModel.loadUserCourseStatus(courseId)
        }
    }

    if (uiState.isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {

                is CourseUiEvent.ShowError -> {
                    viewModel.trackEvent(AnalyticsEvent.DetailCourseError)
                    errorMessage = event.error.toUserMessage()
                    showError = true
                }

                CourseUiEvent.CourseDeleted -> {
                    viewModel.trackEvent(AnalyticsEvent.DetailCourseSuccess)
                    successTitle = "Curso eliminado"
                    successMessage = "El curso se eliminó correctamente."
                    showSuccess = true
                }

                CourseUiEvent.RequestCourse -> {
                    viewModel.trackEvent(AnalyticsEvent.RequestCourse)
                    successTitle = "Solicitud enviada"
                    successMessage = "La solicitud se envió correctamente."
                    showSuccess = true
                }

                else -> {}
            }
        }
    }

    CourseDetailContent(
        uiState = uiState,
        courseId = courseId,
        viewModel = viewModel,
        onDismiss = onDismiss,
        modifier = modifier,
        onEditClick = onEditClick,
        onOpenFacebook = onOpenFacebook,
        onOpenInstagram = onOpenInstagram,
        onOpenWhatsApp = onOpenWhatsApp,
        onAddToCalendar = onAddToCalendar,
        onCreateCourseRequest = {
            viewModel.createRequest(courseId = courseId)
        },
        showConfirmDelete = {
            viewModel.trackEvent(AnalyticsEvent.ShowDialog)
            warningMessage =
                "¿Seguro que deseas eliminar este curso? Esta acción no se puede deshacer."
            showConfirmDelete = true
        }
    )

    if (showConfirmDelete) {
        viewModel.trackEvent(AnalyticsEvent.ShowDialog)
        WarningDialog(
            message = warningMessage,
            onDismiss = {
                viewModel.trackEvent(AnalyticsEvent.HideDialog)
                showConfirmDelete = false
                warningMessage = ""
                viewModel.deleteCourse(courseId = courseId, imageUrl = uiState.courseDetail.imagen)
            },
            onCancel = {
                viewModel.trackEvent(AnalyticsEvent.HideDialog)
                showConfirmDelete = false
                warningMessage = ""
            }
        )
    }

    if (showSuccess) {
        viewModel.trackEvent(AnalyticsEvent.ShowDialog)
        SuccessDialog(
            title = successTitle,
            message = successMessage,
            onDismiss = {
                viewModel.trackEvent(AnalyticsEvent.HideDialog)
                showSuccess = false
                onDismiss()
            },
            onCancel = {}
        )
    }

    if (showError) {
        viewModel.trackEvent(AnalyticsEvent.ShowDialog)
        ErrorDialog(
            message = errorMessage,
            onDismiss = {
                viewModel.trackEvent(AnalyticsEvent.HideDialog)
                showError = false
            },
            onCancel = {}
        )
    }
}