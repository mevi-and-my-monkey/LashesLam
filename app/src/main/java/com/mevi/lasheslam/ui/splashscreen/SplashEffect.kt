package com.mevi.lasheslam.ui.splashscreen

import com.google.android.play.core.appupdate.AppUpdateInfo

sealed class SplashEffect {
    object NavigateHome : SplashEffect()
    object NavigateLogin : SplashEffect()
    data class ForceUpdate(val info: AppUpdateInfo) : SplashEffect()
}