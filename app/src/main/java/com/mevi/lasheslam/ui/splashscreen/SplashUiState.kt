package com.mevi.lasheslam.ui.splashscreen

sealed class SplashUiState {
    object Animating : SplashUiState()
    object Finished : SplashUiState()
}