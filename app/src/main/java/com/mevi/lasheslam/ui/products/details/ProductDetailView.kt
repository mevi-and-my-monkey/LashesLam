package com.mevi.lasheslam.ui.products.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
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
import com.mevi.lasheslam.ui.products.ProductUiEvent
import com.mevi.lasheslam.ui.products.ProductsViewModel

@Composable
fun ProductDetailView(
    productId: String,
    onDismiss: () -> Unit,
    modifier: Modifier,
    onEditClick: (String) -> Unit,
    onOpenFacebook: (String) -> Unit,
    onOpenInstagram: (String) -> Unit,
    onOpenWhatsApp: (String) -> Unit,
    viewModel: ProductsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    var showConfirmDelete by remember { mutableStateOf(false) }
    var showSuccess by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var successTitle by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }
    var warningMessage by remember { mutableStateOf("") }

    LaunchedEffect(productId) {
        viewModel.trackScreen(Screen.ProductDetails.route)
        viewModel.loadCourseById(productId)
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

                is ProductUiEvent.ShowError -> {
                    viewModel.trackEvent(AnalyticsEvent.DetailProductError)
                    errorMessage = event.error.toUserMessage()
                    showError = true
                }

                ProductUiEvent.ProductDeleted -> {
                    viewModel.trackEvent(AnalyticsEvent.DetailProductSuccess)
                    successTitle = "Producto eliminado"
                    successMessage = "El producto se eliminó correctamente."
                    showSuccess = true
                }

                else -> {}
            }
        }
    }

    ProductDetailContent(
        uiState = uiState,
        productId = productId,
        onDismiss = onDismiss,
        modifier = modifier,
        onEditClick = onEditClick,
        onOpenFacebook = onOpenFacebook,
        onOpenInstagram = onOpenInstagram,
        onOpenWhatsApp = onOpenWhatsApp,
        showConfirmDelete = {
            viewModel.trackEvent(AnalyticsEvent.ShowDialog)
            warningMessage =
                "¿Seguro que deseas eliminar este producto? Esta acción no se puede deshacer."
            showConfirmDelete = true
        },
        favoritesList = viewModel.favorites.collectAsState().value
    )

    if (showConfirmDelete) {
        viewModel.trackEvent(AnalyticsEvent.ShowDialog)
        WarningDialog(
            message = warningMessage,
            onDismiss = {
                viewModel.trackEvent(AnalyticsEvent.HideDialog)
                showConfirmDelete = false
                warningMessage = ""
                //viewModel.deleteCourse(courseId = courseId, imageUrl = uiState.courseDetail.imagen)
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