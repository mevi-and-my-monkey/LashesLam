package com.mevi.lasheslam.ui.requestuser

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import com.mevi.lasheslam.ui.components.GenericLoading
import com.mevi.lasheslam.ui.home.components.Section
import com.mevi.lasheslam.ui.requestuser.components.HeaderViewRequestUser

@Composable
fun AdminRequestsUserScreen(
    onNavigateToCourseDetails: (String) -> Unit,
    popBack: () -> Unit,
    viewModel: AdminRequestsUserViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            if (event is RequestUserUiEvent.OpenWhatsApp) {
                context.startActivity(Intent(Intent.ACTION_VIEW, event.url.toUri()))
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier.fillMaxSize()) {

            HeaderViewRequestUser(
                popBack = popBack,
                selectedSection = uiState.selectedSection,
                photoUrl = uiState.photoUser ?: "",
                onSelectSection = { viewModel.onSectionSelected(it) },
                countCourses = uiState.requestUserCourses.size,
                countProducts = uiState.productOrders.size,
                countServices = uiState.reservations.size,
            )

            when (uiState.selectedSection) {
                Section.CURSOS -> UserRequestCursesScreen(
                    onNavigateToCourseDetails = onNavigateToCourseDetails,
                    requestUserCourses = uiState.requestUserCourses
                )

                Section.PRODUCTOS -> UserRequestProductsScreen(
                    productOrders = uiState.productOrders
                )

                Section.SERVICIOS -> UserRequestServicesScreen(
                    reservations = uiState.reservations,
                    clabe = uiState.clabe,
                    bank = uiState.bank,
                    beneficiary = uiState.beneficiary,
                    onSendReceipt = { viewModel.onSendReceipt(it) }
                )
            }
        }

        GenericLoading(
            isLoading = uiState.isLoading,
            message = "Procesando, por favor espera...",
            modifier = Modifier.fillMaxSize()
        )
    }
}