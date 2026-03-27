package com.mevi.lasheslam.ui.splashscreen

import android.app.Activity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.firebase.auth.FirebaseAuth
import com.mevi.lasheslam.core.Strings
import com.mevi.lasheslam.core.results.UpdateResult
import com.mevi.lasheslam.navigation.Screen
import com.mevi.lasheslam.session.SessionManager
import kotlinx.coroutines.delay

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: SplashViewModel = hiltViewModel()
) {
    var showFullName by remember { mutableStateOf(false) }
    var visibleText by remember { mutableStateOf("") }
    val fullText = Strings.appName

    // Animaciones
    val offsetX = remember { Animatable(-200f) }
    val offsetY = remember { Animatable(200f) }

    val context = LocalContext.current
    val activity = context as Activity

    val updateState = viewModel.updateState

    val appUpdateManager = remember {
        AppUpdateManagerFactory.create(context)
    }

    LaunchedEffect(Unit) {
        viewModel.checkUpdate()
    }

    LaunchedEffect(updateState) {
        when (updateState) {

            is UpdateResult.Required -> {
                val info = updateState.appUpdateInfo

                appUpdateManager.startUpdateFlowForResult(
                    info,
                    AppUpdateType.IMMEDIATE,
                    activity,
                    1001
                )
            }

            UpdateResult.NotRequired -> {

                val isLoggedIn = FirebaseAuth.getInstance().currentUser != null
                val currentEmail = FirebaseAuth.getInstance().currentUser?.email ?: ""

                // Animación de las letras
                offsetX.animateTo(0f, tween(800, easing = LinearOutSlowInEasing))
                offsetY.animateTo(0f, tween(800, easing = LinearOutSlowInEasing))
                delay(300)
                showFullName = true
                fullText.forEachIndexed { index, _ ->
                    visibleText = fullText.take(index + 1)
                    delay(100)
                }

                SessionManager.refreshAdmins()

                if (isLoggedIn) {
                    val isAdmin = SessionManager.isAdmin(currentEmail)
                    SessionManager.setAdmin(isAdmin)
                    SessionManager.setInvited(false)
                }

                val firstPage = if (isLoggedIn) Screen.Home.route else Screen.Login.route

                navController.navigate(firstPage) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            }

            null -> {}
        }
    }
    SplashAnimation(showFullName, visibleText, offsetX, offsetY)
}