package com.mevi.lasheslam.ui.home.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.mevi.lasheslam.domain.analytics.AnalyticsEvent
import com.mevi.lasheslam.navigation.Screen
import com.mevi.lasheslam.network.LocationItem
import com.mevi.lasheslam.ui.common.toUserMessage
import com.mevi.lasheslam.ui.components.pickers.DatePickerDialogCustom
import com.mevi.lasheslam.ui.components.pickers.TimePickerDialog
import com.mevi.lasheslam.ui.courses.AddCoursesContent
import com.mevi.lasheslam.ui.home.cursos.CourseUiEvent
import com.mevi.lasheslam.ui.home.cursos.CourseViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CourseAddView(
    onDismiss: () -> Unit,
    linkedBannerIndex: Int,
    viewModel: CourseViewModel = hiltViewModel()
) {

    val state by viewModel.uiState.collectAsState()
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showSuccess by remember { mutableStateOf(false) }
    var successMessage by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.trackScreen(Screen.Courses.route)
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {

                is CourseUiEvent.CourseSaved -> {
                    viewModel.trackEvent(AnalyticsEvent.SaveCourseSuccess)
                    successMessage = "Curso guardado correctamente"
                    showSuccess = true

                }

                is CourseUiEvent.ShowError -> {
                    viewModel.trackEvent(AnalyticsEvent.SaveCourseError)
                    errorMessage = event.error.toUserMessage()
                    showError = true
                }

                else -> {}
            }
        }
    }

    var expanded by remember { mutableStateOf(false) }
    var selectedLocation by remember { mutableStateOf<LocationItem?>(null) }

    var showTimePickerInicio by remember { mutableStateOf(false) }
    var showTimePickerFin by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    AddCoursesContent(
        state = state,
        onDismiss = onDismiss,
        showSuccess = showSuccess,
        successMessage = successMessage,
        showError = showError,
        errorMessage = errorMessage,
        onShowTimePickerInicio = {
            showTimePickerInicio = true
        },
        onShowTimePickerFin = {
            showTimePickerFin = true
        },
        onShowDatePicker = {
            showDatePicker = true
        },
        onImageChange = { uri -> viewModel.onImageChange(uri) },
        onInstructorImageChange = { uri -> viewModel.onInstructorImageChange(uri) },
        onTitleChange = { title -> viewModel.onTitleChange(title) },
        onDescriptionChange = { description -> viewModel.onDescriptionChange(description) },
        onCostChange = { cost -> viewModel.onCostChange(cost) },
        onApartaChange = { aparta -> viewModel.onApartaChange(aparta) },
        onInstructorChange = { instructor -> viewModel.onInstructorChange(instructor) },
        onInstructorDescChange = { instructorDesc ->
            viewModel.onInstructorDescChange(
                instructorDesc
            )
        },
        onTemarioChange = { index, value -> viewModel.onTemarioChange(index, value) },
        saveCourse = { viewModel.saveCourse(selectedLocation, linkedBannerIndex) },
        locations = state.locations,
        expanded = expanded,
        selectedLocation = selectedLocation,
        onExpandedChange = { expanded = it },
        onLocationSelected = { selectedLocation = it },
        onDismissSuccess = {
            showSuccess = false
            onDismiss()
        },
        onDismissError = {
            showError = false
        }
    )
    if (showTimePickerInicio) {
        TimePickerDialog(
            onTimeSelected = { viewModel.onHourInitialChange(it) },
            onDismiss = { showTimePickerInicio = false }
        )
    }
    if (showTimePickerFin) {
        TimePickerDialog(
            onTimeSelected = { viewModel.onHourFinalChange(it) },
            onDismiss = { showTimePickerFin = false }
        )
    }
    if (showDatePicker) {
        DatePickerDialogCustom(
            onDateSelected = { viewModel.onDateChange(it) },
            onDismiss = { showDatePicker = false }
        )
    }
}