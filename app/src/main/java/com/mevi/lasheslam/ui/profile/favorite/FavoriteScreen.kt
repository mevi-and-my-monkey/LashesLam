package com.mevi.lasheslam.ui.profile.favorite

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mevi.lasheslam.R
import com.mevi.lasheslam.ui.components.GenericLoading
import com.mevi.lasheslam.ui.components.dialogs.DialogComingSon
import com.mevi.lasheslam.ui.profile.HeaderViewRequest
import com.mevi.lasheslam.ui.profile.Section
import com.mevi.lasheslam.ui.profile.favorite.components.HeaderViewFav
import com.mevi.lasheslam.ui.profile.request.AdmRequestCursesScreen
import com.mevi.lasheslam.ui.profile.request.AdminRequestsViewModel

@Composable
fun FavoriteScreen(
    popBack: () -> Unit,
    onNavigateToCourseDetails: (String) -> Unit,
    onNavigateToProductsDetail: (String) -> Unit,
    onNavigateToServiceEdit: (String) -> Unit,
    viewModel: AdminRequestsViewModel = hiltViewModel()
) {
    var selectedSection by remember { mutableStateOf(Section.CURSOS) }
    val isLoading: Boolean by viewModel.isLoading.observeAsState(initial = false)

    Box(modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier.fillMaxSize()) {

            HeaderViewFav(
                selectedSection = selectedSection,
                popBack = popBack,
                onSelectSection = { selectedSection = it }
            )

            when (selectedSection) {
                Section.CURSOS -> FavoriteCoursesScreen(
                    onNavigateToCourseDetails = onNavigateToCourseDetails
                )
                Section.PRODUCTOS -> {

                }

                Section.SERVICIOS -> {
                }
            }
        }

        GenericLoading(
            isLoading = isLoading,
            message = "Procesando, por favor espera...",
            modifier = Modifier.fillMaxSize()
        )
    }
}