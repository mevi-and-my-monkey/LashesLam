package com.mevi.lasheslam.ui.services.add

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
import com.mevi.lasheslam.ui.common.toUserMessage
import com.mevi.lasheslam.ui.services.ServiceUiEvent
import com.mevi.lasheslam.ui.services.ServicesViewModel

@Composable
fun AddServicesScreen(
    onDismiss: () -> Unit,
    viewModel: ServicesViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showSuccess by remember { mutableStateOf(false) }
    var successMessage by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.trackScreen(Screen.Service.route)
    }
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is ServiceUiEvent.ShowError -> {
                    viewModel.trackEvent(AnalyticsEvent.SaveServiceError)
                    errorMessage = event.error.toUserMessage()
                    showError = true
                }
                ServiceUiEvent.ServiceSaved -> {
                    viewModel.trackEvent(AnalyticsEvent.SaveServiceSuccess)
                    successMessage = "Servicio guardado correctamente"
                    showSuccess = true
                }

                else -> {}
            }
        }
    }

    AddServiceContent(
        state = state,
        onDismiss = onDismiss,
        showSuccess = showSuccess,
        successMessage = successMessage,
        showError = showError,
        errorMessage = errorMessage,
        onDismissSuccess = {
            showSuccess = false
            onDismiss()
        },
        onDismissError = {
            showError = false
        },
        saveService = viewModel::saveService,
        onTitleChange = viewModel::onTitleChange,
        onSubtitleChange = viewModel::onSubtitleChange,
        onCostChange = viewModel::onCostChange,
        onDurationChange = viewModel::onDurationChange,
        onCategoryChange = viewModel::onCategoryChange,
        onDescriptionChange = viewModel::onDescriptionChange,
        onIncludesChange = viewModel::onIncludesChange,
        onImageChange = viewModel::onImageChange
    )
}