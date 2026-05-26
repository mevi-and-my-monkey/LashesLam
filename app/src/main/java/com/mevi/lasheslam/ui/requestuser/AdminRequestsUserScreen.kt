package com.mevi.lasheslam.ui.requestuser

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.mevi.lasheslam.ui.components.GenericLoading
import com.mevi.lasheslam.ui.home.components.Section
import com.mevi.lasheslam.ui.requestuser.components.HeaderViewRequestUser

@Composable
fun AdminRequestsUserScreen(
    onNavigateToCourseDetails: () -> Unit,
    popBack: () -> Unit,
    viewModel: AdminRequestsUserViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier.fillMaxSize()) {

            HeaderViewRequestUser(
                popBack = popBack,
                selectedSection = uiState.selectedSection,
                photoUrl = uiState.photoUser ?: "",
                onSelectSection = { viewModel.onSectionSelected(it) },
                countCourses = uiState.requestUserCourses.size,
            )

            when (uiState.selectedSection) {
                Section.CURSOS -> UserRequestCursesScreen(
                    onNavigateToCourseDetails = onNavigateToCourseDetails,
                    requestUserCourses = uiState.requestUserCourses
                )

                Section.PRODUCTOS -> {}
                Section.SERVICIOS -> {}
            }
        }

        GenericLoading(
            isLoading = uiState.isLoading,
            message = "Procesando, por favor espera...",
            modifier = Modifier.fillMaxSize()
        )
    }
}