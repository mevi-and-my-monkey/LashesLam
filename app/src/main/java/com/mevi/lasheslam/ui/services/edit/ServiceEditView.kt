package com.mevi.lasheslam.ui.services.edit

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mevi.lasheslam.R
import com.mevi.lasheslam.domain.analytics.AnalyticsEvent
import com.mevi.lasheslam.navigation.Screen
import com.mevi.lasheslam.ui.common.toUserMessage
import com.mevi.lasheslam.ui.components.ErrorDialog
import com.mevi.lasheslam.ui.components.SuccessDialog
import com.mevi.lasheslam.ui.components.WarningDialog
import com.mevi.lasheslam.ui.services.ServiceUiEvent
import com.mevi.lasheslam.ui.services.ServicesViewModel
import com.mevi.lasheslam.ui.services.add.components.AddIProdmageView
import com.mevi.lasheslam.ui.services.add.components.AddServCategoryFormView
import com.mevi.lasheslam.ui.services.add.components.AddServCostFormView
import com.mevi.lasheslam.ui.services.add.components.AddServTitleFormView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceEditView(
    serviceId: String,
    onDismiss: () -> Unit,
    onfinish: () -> Unit,
    viewModel: ServicesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(serviceId) {
        viewModel.trackScreen(Screen.ServiceEdit.route)
        viewModel.loadServiceById(serviceId)
    }

    var showSuccess by remember { mutableStateOf(false) }
    var successMessage by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    var showConfirmDelete by remember { mutableStateOf(false) }
    var warningMessage by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {

                is ServiceUiEvent.ServiceUpdated -> {
                    viewModel.trackEvent(AnalyticsEvent.UpdateServiceSuccess)
                    successMessage = "servicio actualizado correctamente"
                    showSuccess = true

                }

                is ServiceUiEvent.ShowError -> {
                    viewModel.trackEvent(AnalyticsEvent.UpdateServiceError)
                    errorMessage = event.error.toUserMessage()
                    showError = true
                }

                else -> {}
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.edit_product_title)) },
                navigationIcon = {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ) {
                AddIProdmageView(state = uiState, onImageChange = { viewModel.onImageChange(it) })

                AddServTitleFormView(
                    state = uiState,
                    onTitleChange = { viewModel.onTitleChange(it) },
                    onSubtitleChange = { viewModel.onSubtitleChange(it) }
                )

                AddServCostFormView(
                    state = uiState,
                    onCostChange = { viewModel.onCostChange(it) },
                    onDurationChange = { viewModel.onDurationChange(it) }
                )

                AddServCategoryFormView(
                    state = uiState,
                    onCategoryChange = { viewModel.onCategoryChange(it) })

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { viewModel.updateService() }
                ) {
                    Text(
                        if (uiState.isLoading) stringResource(R.string.saving) else stringResource(
                            R.string.save_changes
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        viewModel.trackEvent(AnalyticsEvent.ShowDialog)
                        warningMessage =
                            "¿Seguro que deseas eliminar este servicio? Esta acción no se puede deshacer."
                        showConfirmDelete = true
                    }
                ) {
                    Text(stringResource(R.string.delete))
                }
            }
        }
    }

    if (showSuccess) {
        SuccessDialog(
            title = stringResource(R.string.update),
            message = successMessage,
            onDismiss = {
                showSuccess = false
                onfinish()
            },
            onCancel = {}
        )
    }

    if (showError) {
        ErrorDialog(
            title = stringResource(R.string.error),
            message = errorMessage ?: "",
            onDismiss = {
                showError = false
                onDismiss()
            },
            onCancel = {}
        )
    }

    if (showConfirmDelete) {
        viewModel.trackEvent(AnalyticsEvent.ShowDialog)
        WarningDialog(
            message = warningMessage,
            onDismiss = {
                viewModel.trackEvent(AnalyticsEvent.HideDialog)
                showConfirmDelete = false
                warningMessage = ""
                viewModel.deleteService(serviceId = serviceId, imageUrl = uiState.form.remoteImage)
            },
            onCancel = {
                viewModel.trackEvent(AnalyticsEvent.HideDialog)
                showConfirmDelete = false
                warningMessage = ""
            }
        )
    }
}