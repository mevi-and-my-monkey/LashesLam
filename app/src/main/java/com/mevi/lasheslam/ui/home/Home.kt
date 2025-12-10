package com.mevi.lasheslam.ui.home

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.mevi.lasheslam.navigation.Screen
import com.mevi.lasheslam.session.SessionManager
import com.mevi.lasheslam.ui.auth.LoginViewModel
import com.mevi.lasheslam.ui.components.FloatingBottomNavigation
import com.mevi.lasheslam.ui.components.GenericLoading
import com.mevi.lasheslam.ui.products.ProductsView
import com.mevi.lasheslam.ui.profile.ProfilePage

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    navController: NavHostController,
    loginViewModel: LoginViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    var selectedIndex by remember { mutableIntStateOf(0) }
    val isLoading: Boolean by loginViewModel.isLoading.observeAsState(initial = false)
    val isLoadingHome: Boolean by homeViewModel.isLoading.observeAsState(initial = false)
    val isAdmin by SessionManager.isUserAdmin.collectAsState()

    LaunchedEffect(Unit) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val email = FirebaseAuth.getInstance().currentUser?.email
        SessionManager.setCurrentUserId(uid)
        SessionManager.setEmailUser(email)
    }

    BackHandler {
        if (selectedIndex != 0) {
            selectedIndex = 0
        } else {
            //showExitDialog = true
        }
    }
    Scaffold(
        bottomBar = {
            FloatingBottomNavigation(
                selectedIndex = selectedIndex,
                onItemSelected = { selectedIndex = it }
            )
        }
    ) { paddingValues ->
        // Contenido de la pantalla
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding())
        ) {


            when (selectedIndex) {
                0 -> HomePage(navController)
                1 -> navController.navigate(Screen.Search.route)
                2 -> Text("Favoritos")
                3 -> {
                    if (isAdmin) {
                        navController.navigate(Screen.Request.route)
                    } else {
                        //navController.navigate(Screen.UserCourses.route)
                    }
                }

                4 -> ProfilePage(navController)
            }

            GenericLoading(
                isLoading = isLoading,
                message = "Procesando, por favor espera..."
            )
            GenericLoading(
                isLoading = isLoadingHome,
                message = "Procesando, por favor espera..."
            )
        }
    }
}