package com.mevi.lasheslam.utils

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class NotificationWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters
) : Worker(context, params) {

    override fun doWork(): Result {
        val title = inputData.getString("title") ?: return Result.failure()
        val message = inputData.getString("message") ?: return Result.failure()

        showNotification(
            context = applicationContext,
            title = title,
            message = message
        )

        return Result.success()
    }
}