package com.mevi.lasheslam.ui.splashscreen

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.mevi.lasheslam.core.Strings
import com.mevi.lasheslam.ui.update.PlayCoreUpdateLauncher
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SplashScreen(
    modifier: Modifier,
    playCoreUpdateLauncher: PlayCoreUpdateLauncher,
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

    var pendingEffect by remember { mutableStateOf<SplashEffect?>(null) }
    var uiState by remember { mutableStateOf<SplashUiState>(SplashUiState.Animating) }

    val updateActivityLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode != Activity.RESULT_OK) {
            // Si la actualización es obligatoria, aquí podrías mostrar un diálogo,
            // reintentar o mantener al usuario en Splash.
        }
    }

    LaunchedEffect(Unit) {
        offsetX.animateTo(0f, tween(800, easing = LinearOutSlowInEasing))
        offsetY.animateTo(0f, tween(800, easing = LinearOutSlowInEasing))

        delay(300.milliseconds)
        showFullName = true

        fullText.forEachIndexed { index, _ ->
            visibleText = fullText.take(index + 1)
            delay(200.milliseconds)
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

        when (pendingEffect) {
            SplashEffect.NavigateHome -> {
                onNavigateToHome()
                pendingEffect = null
            }

            SplashEffect.NavigateLogin -> {
                onNavigateToLogin()
                pendingEffect = null
            }

            SplashEffect.ForceUpdate -> {
                playCoreUpdateLauncher.launchImmediateUpdate(updateActivityLauncher)
                pendingEffect = null
            }

            null -> Unit
        }
    }

    SplashAnimation(showFullName, visibleText, offsetX.value, offsetY.value, modifier)

}