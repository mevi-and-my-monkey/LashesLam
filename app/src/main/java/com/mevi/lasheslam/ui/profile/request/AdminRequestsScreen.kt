package com.mevi.lasheslam.ui.profile.request

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.mevi.lasheslam.ui.components.GenericLoading
import com.mevi.lasheslam.ui.profile.HeaderViewRequest
import com.mevi.lasheslam.ui.profile.Section

@Composable
fun AdminRequestsScreen(
    popBack: () -> Unit,
    viewModel: AdminRequestsViewModel = hiltViewModel(),
    ordersViewModel: AdminProductOrdersViewModel = hiltViewModel(),
    reservationsViewModel: AdminReservationsViewModel = hiltViewModel()
) {
    var selectedSection by remember { mutableStateOf(Section.CURSOS) }
    val isLoading: Boolean by viewModel.isLoading.observeAsState(initial = false)

    LaunchedEffect(Unit) {
        viewModel.loadRequests("pendiente")
        ordersViewModel.loadOrders()
        reservationsViewModel.loadReservations()
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier.fillMaxSize()) {

            HeaderViewRequest(
                popBack = popBack,
                selectedSection = selectedSection,
                onSelectSection = { selectedSection = it },
                countCourses = viewModel.requests.size,
                countProducts = ordersViewModel.pendingCount,
                countServices = reservationsViewModel.pendingCount
            )

            when (selectedSection) {
                Section.CURSOS -> AdmRequestCursesScreen()
                Section.PRODUCTOS -> AdmRequestProductsScreen()
                Section.SERVICIOS -> AdmRequestServicesScreen()
            }
        }

        GenericLoading(
            isLoading = isLoading,
            message = "Procesando, por favor espera...",
            modifier = Modifier.fillMaxSize()
        )
    }
}
