package com.mevi.lasheslam.ui.update

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PlayCoreUpdateLauncher @Inject constructor(
    private val appUpdateManager: AppUpdateManager
) {

    suspend fun launchImmediateUpdate(
        launcher: ActivityResultLauncher<IntentSenderRequest>
    ): Boolean {
        return runCatching {
            val info = appUpdateManager.appUpdateInfo.await()

            val isUpdateAvailable =
                info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE

            val isImmediateUpdateAllowed =
                info.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)

            if (!isUpdateAvailable || !isImmediateUpdateAllowed) {
                return@runCatching false
            }

            appUpdateManager.startUpdateFlowForResult(
                info,
                launcher,
                AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
            )
        }.getOrDefault(false)
    }
}