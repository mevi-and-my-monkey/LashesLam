package com.mevi.lasheslam.ui.splashscreen


sealed class SplashEffect {
    object ForceUpdate : SplashEffect()
    object NavigateHome : SplashEffect()
    object NavigateLogin : SplashEffect()
}