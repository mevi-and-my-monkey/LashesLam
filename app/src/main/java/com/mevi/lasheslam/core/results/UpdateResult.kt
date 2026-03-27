package com.mevi.lasheslam.core.results

import com.google.android.play.core.appupdate.AppUpdateInfo

sealed class UpdateResult {
    data class Required(val appUpdateInfo: AppUpdateInfo) : UpdateResult()
    object NotRequired : UpdateResult()
}