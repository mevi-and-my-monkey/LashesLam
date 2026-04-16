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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.mevi.lasheslam.core.Strings
import kotlinx.coroutines.delay

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SplashScreen(
    modifier: Modifier,
    viewModel: SplashViewModel = hiltViewModel(),
    onNavigateToHome: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var showFullName by remember { mutableStateOf(false) }
    var visibleText by remember { mutableStateOf("") }
    val fullText = Strings.appName

    // Animaciones
    val offsetX = remember { Animatable(-200f) }
    val offsetY = remember { Animatable(200f) }

    val context = LocalContext.current
    val activity = context as? Activity ?: return

    var pendingEffect by remember { mutableStateOf<SplashEffect?>(null) }
    var uiState by remember { mutableStateOf<SplashUiState>(SplashUiState.Animating) }

    val appUpdateManager = remember {
        AppUpdateManagerFactory.create(context)
    }

    LaunchedEffect(Unit) {
        offsetX.animateTo(0f, tween(800, easing = LinearOutSlowInEasing))
        offsetY.animateTo(0f, tween(800, easing = LinearOutSlowInEasing))

        delay(300)
        showFullName = true

        fullText.forEachIndexed { index, _ ->
            visibleText = fullText.take(index + 1)
            delay(200)
        }

        uiState = SplashUiState.Finished
    }

    LaunchedEffect(Unit) {
        viewModel.init()
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            pendingEffect = effect
        }
    }

    LaunchedEffect(uiState, pendingEffect) {
        if (uiState != SplashUiState.Finished) return@LaunchedEffect

        when (val effect = pendingEffect) {
            SplashEffect.NavigateHome -> onNavigateToHome()
            SplashEffect.NavigateLogin -> onNavigateToLogin()

            is SplashEffect.ForceUpdate -> {
                appUpdateManager.startUpdateFlowForResult(
                    effect.info,
                    AppUpdateType.IMMEDIATE,
                    activity,
                    1001
                )
            }

            null -> {}
        }
    }

    SplashAnimation(showFullName, visibleText, offsetX.value, offsetY.value, modifier)

}