package com.mevi.lasheslam.ui.products.edit

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.mevi.lasheslam.ui.courses.components.AddTitleView
import com.mevi.lasheslam.ui.products.ProductUiEvent
import com.mevi.lasheslam.ui.products.ProductsViewModel
import com.mevi.lasheslam.ui.products.add.components.AddProdCategoryFormView
import com.mevi.lasheslam.ui.products.add.components.AddProdCostFormView
import com.mevi.lasheslam.ui.products.add.components.AddProdImagesView
import com.mevi.lasheslam.ui.products.add.components.AddProdTitleFormView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductEditView(
    productId: String,
    onDismiss: () -> Unit,
    onfinish: () -> Unit,
    viewModel: ProductsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(productId) {
        viewModel.trackScreen(Screen.ProductEdit.route)
        viewModel.loadProductById(productId)
    }

    var showSuccess by remember { mutableStateOf(false) }
    var successMessage by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {

                is ProductUiEvent.ProductUpdated -> {
                    viewModel.trackEvent(AnalyticsEvent.UpdateProductSuccess)
                    successMessage = "Producto actualizado correctamente"
                    showSuccess = true

                }

                is ProductUiEvent.ShowError -> {
                    viewModel.trackEvent(AnalyticsEvent.UpdateProductError)
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

                AddTitleView(stringResource(R.string.product_add_title))

                AddProdImagesView(
                    state = uiState,
                    onAddImages = { viewModel.onImagesSelected(it) },
                    onRemoveImage = { viewModel.removeImage(it) },
                    removeRemoteImage = { viewModel.removeRemoteImage(it) },
                )

                AddProdTitleFormView(
                    uiState,
                    onTitleChange = { viewModel.onTitleChange(it) },
                    onDescriptionChange = { viewModel.onDescriptionChange(it) },
                    onCharacteristicsChange = { viewModel.onCharacteristicsChange(it) }
                )

                AddProdCostFormView(
                    uiState,
                    onCostChange = { viewModel.onCostChange(it) },
                    onActualCostChange = { viewModel.onActualCostChange(it) }
                )

                AddProdCategoryFormView(
                    uiState,
                    onCategoryChange = { viewModel.onCategoryChange(it) },
                    onBestSellingChange = { viewModel.onBestSellingChange(it) })


                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { viewModel.updateProduct() }
                ) {
                    Text(
                        if (uiState.isLoading) stringResource(R.string.saving) else stringResource(
                            R.string.save_changes
                        )
                    )
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

}