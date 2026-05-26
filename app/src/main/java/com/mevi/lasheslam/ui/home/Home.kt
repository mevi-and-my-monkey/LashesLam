package com.mevi.lasheslam.ui.home

import android.app.Activity
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.mevi.lasheslam.R
import com.mevi.lasheslam.domain.analytics.AnalyticsEvent
import com.mevi.lasheslam.navigation.Screen
import com.mevi.lasheslam.session.SessionManager
import com.mevi.lasheslam.ui.components.FloatingBottomNavigation
import com.mevi.lasheslam.ui.components.GenericLoading
import com.mevi.lasheslam.ui.components.WarningDialog
import com.mevi.lasheslam.ui.components.views.EmptyViewScreen
import com.mevi.lasheslam.ui.profile.ProfilePage

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    onNavigateToSearch: () -> Unit,
    onNavigateToFavorite: () -> Unit,
    onNavigateToRequest: () -> Unit,
    onNavigateToUserRequest: () -> Unit,
    onNavigateToCourses: () -> Unit,
    onNavigateToLogOut: () -> Unit,
    onNavigateToCourseDetails: (String) -> Unit,
    onNavigateToProductsDetail: (String) -> Unit,
    onNavigateToServiceEdit: (String) -> Unit,
    onOpenWhatsApp: (String) -> Unit,
    modifier: Modifier,
    viewModel: HomePageViewModel = hiltViewModel()
) {
    val activity = (LocalContext.current as? Activity)

    var selectedIndex by remember { mutableIntStateOf(0) }
    val uiState by viewModel.uiState.collectAsState()

    val isAdmin by SessionManager.isUserAdmin.collectAsState()
    var showExitDialog by remember { mutableStateOf(false) }

    BackHandler {
        if (selectedIndex != 0) {
            selectedIndex = 0
        } else {
            showExitDialog = true
        }
    }
    Scaffold(
        modifier = modifier,
        bottomBar = {
            FloatingBottomNavigation(
                selectedIndex = selectedIndex,
                onItemSelected = { index ->
                    selectedIndex = index
                    when (index) {
                        1 -> {
                            viewModel.trackEvent(AnalyticsEvent.BottomSelection(Screen.Favorite.route))
                            viewModel.trackScreen(Screen.Favorite.route)
                            onNavigateToFavorite()
                        }

                        2 -> {
                            if (isAdmin) {
                                viewModel.trackEvent(AnalyticsEvent.BottomSelection(Screen.Request.route))
                                viewModel.trackScreen(Screen.Request.route)
                                onNavigateToRequest()
                            } else {
                                viewModel.trackEvent(AnalyticsEvent.BottomSelection(Screen.RequestUser.route))
                                viewModel.trackScreen(Screen.RequestUser.route)
                                onNavigateToUserRequest()
                            }
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding())
        ) {
            when (selectedIndex) {
                0 -> {
                    viewModel.trackEvent(AnalyticsEvent.BottomSelection(Screen.Home.route))
                    viewModel.trackScreen(Screen.Home.route)
                    HomePage(
                        onNavigateToRequest = onNavigateToRequest,
                        onNavigateToRequestUser = onNavigateToUserRequest,
                        onNavigateToSearch = onNavigateToSearch,
                        onNavigateToServiceDetails = onNavigateToCourseDetails,
                        onNavigateToProductsDetail = onNavigateToProductsDetail,
                        onNavigateToServiceEdit = onNavigateToServiceEdit,
                        onOpenWhatsApp = onOpenWhatsApp
                    )
                }

                2 -> if (!isAdmin) EmptyViewScreen()
                3 -> {
                    viewModel.trackEvent(AnalyticsEvent.BottomSelection(Screen.Profile.route))
                    viewModel.trackScreen(Screen.Profile.route)
                    ProfilePage(
                        onNavigateToFavorite = onNavigateToFavorite,
                        onNavigateToRequest = onNavigateToRequest,
                        onNavigateToRequestUser = onNavigateToUserRequest,
                        onNavigateToCourses = onNavigateToCourses,
                        onNavigateToLogOut = onNavigateToLogOut
                    )
                }
            }
            GenericLoading(
                isLoading = uiState.isLoading,
                message = stringResource(R.string.loading_generic)
            )
        }
    }
    if (showExitDialog) {
        WarningDialog(
            message = stringResource(R.string.exit_warning),
            onDismiss = {
                activity?.finish()
                showExitDialog = false
            },
            onCancel = {
                showExitDialog = false
            }
        )
    }
}