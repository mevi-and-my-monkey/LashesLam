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
import com.mevi.lasheslam.ui.profile.request.AdmRequestCursesScreen
import com.mevi.lasheslam.ui.profile.request.AdminRequestsViewModel

@Composable
fun FavoriteScreen(
    navController: NavController,
    viewModel: AdminRequestsViewModel = hiltViewModel()
) {
    var selectedSection by remember { mutableStateOf(Section.CURSOS) }
    val isLoading: Boolean by viewModel.isLoading.observeAsState(initial = false)
    var showDialogComingSoon by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier.fillMaxSize()) {

            HeaderViewRequest(
                navController = navController,
                selectedSection = selectedSection,
                onSelectSection = { selectedSection = it }
            )

            when (selectedSection) {
                Section.CURSOS -> FavoriteCoursesScreen(navController = navController)
                Section.PRODUCTOS -> {
                    showDialogComingSoon = true
                }
                Section.SERVICIOS -> {
                    showDialogComingSoon = true
                }
            }
        }

        GenericLoading(
            isLoading = isLoading,
            message = "Procesando, por favor espera...",
            modifier = Modifier.fillMaxSize()
        )
    }

    if (showDialogComingSoon) {
        DialogComingSon(
            onDismiss = {
                showDialogComingSoon = false
                selectedSection = Section.CURSOS
            },
            drawableRes = R.drawable.ic_star,
            title = stringResource(R.string.title_coming_soon),
            content = stringResource(R.string.content_coming_soon),
            textButton = stringResource(R.string.button_understand)
        )
    }
}