package com.mevi.lasheslam.ui.products.add

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
import com.mevi.lasheslam.ui.products.ProductUiEvent
import com.mevi.lasheslam.ui.products.ProductsViewModel

@Composable
fun ProductsAddScreen(
    onDismiss: () -> Unit,
    viewModel: ProductsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showSuccess by remember { mutableStateOf(false) }
    var successMessage by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.trackScreen(Screen.Product.route)
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is ProductUiEvent.ShowError -> {
                    viewModel.trackEvent(AnalyticsEvent.SaveProductError)
                    errorMessage = event.error.toUserMessage()
                    showError = true
                }

                ProductUiEvent.ProductSaved -> {
                    viewModel.trackEvent(AnalyticsEvent.SaveProductSuccess)
                    successMessage = "Producto guardado correctamente"
                    showSuccess = true
                }

                else -> {}
            }
        }
    }

    AddProductsContent(
        state = state,
        onDismiss = onDismiss,
        onTitleChange = viewModel::onTitleChange,
        onCharacteristicsChange = viewModel::onCharacteristicsChange,
        onDescriptionChange = viewModel::onDescriptionChange,
        onCostChange = viewModel::onCostChange,
        onActualCostChange = viewModel::onActualCostChange,
        onCategoryChange = viewModel::onCategoryChange,
        onBestSellingChange = viewModel::onBestSellingChange,
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
        saveProduct = viewModel::saveProduct,
        onImagesSelected = viewModel::onImagesSelected,
        removeImage = viewModel::removeImage,
        removeRemoteImage = viewModel::removeRemoteImage
    )
}