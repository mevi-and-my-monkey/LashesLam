package com.mevi.lasheslam.data

import android.content.Context
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.mevi.lasheslam.domain.repository.UpdateRepository
import com.mevi.lasheslam.core.results.UpdateResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PlayCoreUpdateRepository @Inject constructor(
    @ApplicationContext private val context: Context
) : UpdateRepository {

    private val manager = AppUpdateManagerFactory.create(context)

    override suspend fun getUpdateInfo(): UpdateResult {
        return try {
            val info = manager.appUpdateInfo.await()

            val isAvailable =
                info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE

            val isImmediate =
                info.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)

            if (isAvailable && isImmediate) {
                UpdateResult.Required(info)
            } else {
                UpdateResult.NotRequired
            }

        } catch (e: Exception) {
            UpdateResult.NotRequired
        }
    }
}