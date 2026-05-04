package com.mevi.lasheslam.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.mevi.lasheslam.R
import com.mevi.lasheslam.navigation.Screen
import com.mevi.lasheslam.ui.components.RequestNotificationPermission
import com.mevi.lasheslam.ui.components.dialogs.DialogComingSon
import com.mevi.lasheslam.ui.home.components.Section

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomePage(
    onNavigateToSearch: () -> Unit,
    onNavigateToRequest: () -> Unit,
    onNavigateToServiceDetails: (String) -> Unit,
    viewModel: HomePageViewModel = hiltViewModel()
) {
    RequestNotificationPermission()
    val uiState by viewModel.uiState.collectAsState()
    var dialogState by remember { mutableStateOf<HomeUiEvent?>(null) }

    LaunchedEffect(viewModel) {
        viewModel.events.collect { events ->
            dialogState = events
        }
    }

    LaunchedEffect(Unit) {
        viewModel.trackScreen(Screen.Home.route)
    }

    HomePageContent(
        state = uiState,
        onNavigateToRequest = onNavigateToRequest,
        onNavigateToSearch = onNavigateToSearch,
        onNavigateToServiceDetails = onNavigateToServiceDetails,
        onSelectedSection = { section ->
            viewModel.onSectionSelected(section)
        },
        trackEvent = { event ->
            viewModel.trackEvent(event)
        },
        trackScreen = { screen ->
            viewModel.trackScreen(screen)
        },
        selectedCategoryId = viewModel.selectedCategoryId,
        onCategorySelected = { viewModel.onCategorySelected(it) },
        selectedServiceCategoryId = viewModel.selectedServiceCategoryId,
        onCategoryServiceSelected = { viewModel.onCategoryServiceSelected(it) }
    )

    if (dialogState is HomeUiEvent.ShowComingSoon) {
        DialogComingSon(
            onDismiss = {
                dialogState = null
                viewModel.onSectionSelected(Section.CURSOS)
            },
            drawableRes = R.drawable.ic_star,
            title = stringResource(R.string.title_coming_soon),
            content = stringResource(R.string.content_coming_soon),
            textButton = stringResource(R.string.button_understand)
        )
    }

}