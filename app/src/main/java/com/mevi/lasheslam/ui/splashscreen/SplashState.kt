package com.mevi.lasheslam.ui.splashscreen

import com.google.android.play.core.appupdate.AppUpdateInfo

sealed class SplashState {
    object Loading : SplashState()
    data class ForceUpdate(val info: AppUpdateInfo) : SplashState()
    object GoToHome : SplashState()
    object GoToLogin : SplashState()
}