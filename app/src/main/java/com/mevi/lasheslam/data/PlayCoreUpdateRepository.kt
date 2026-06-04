package com.mevi.lasheslam.data

import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.mevi.lasheslam.core.results.UpdateResult
import com.mevi.lasheslam.domain.repository.UpdateRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PlayCoreUpdateRepository @Inject constructor(
    private val appUpdateManager: AppUpdateManager
) : UpdateRepository {

    override suspend fun checkForUpdate(): UpdateResult {
        return try {
            val info = appUpdateManager.appUpdateInfo.await()

            val isUpdateAvailable =
                info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE

            val isImmediateUpdateAllowed =
                info.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)

            if (isUpdateAvailable && isImmediateUpdateAllowed) {
                UpdateResult.Required
            } else {
                UpdateResult.NotRequired
            }
        } catch (e: Exception) {
            UpdateResult.NotRequired
        }
    }
}
