package com.mevi.lasheslam.data.notifications

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.*
import com.mevi.lasheslam.domain.repository.NotificationScheduler
import com.mevi.lasheslam.utils.NotificationWorker
import com.mevi.lasheslam.utils.showNotification
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.Duration
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class NotificationSchedulerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : NotificationScheduler {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun schedule(
        courseId: String,
        courseName: String,
        startDateTime: LocalDateTime
    ): Boolean {
        val now = LocalDateTime.now()
        var scheduled = false

        val notifications = listOf(
            startDateTime.minusDays(2) to "Tu curso $courseName es en 2 días",
            startDateTime.minusDays(1) to "Tu curso $courseName es mañana",
            startDateTime.minusHours(2) to "Tu curso $courseName inicia en 2 horas",
            startDateTime to "Tu curso $courseName inicia ahora"
        )

        notifications.forEachIndexed { index, (time, message) ->
            val delay = Duration.between(now, time).toMillis()

            if (delay > 0) {
                scheduled = true
                val work = OneTimeWorkRequestBuilder<NotificationWorker>()
                    .addTag(courseId)
                    .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                    .setInputData(
                        workDataOf(
                            "title" to "Curso próximo",
                            "message" to message
                        )
                    ).build()

                WorkManager.getInstance(context)
                    .enqueueUniqueWork(
                        "$courseId-$index",
                        ExistingWorkPolicy.REPLACE,
                        work
                    )
            }
        }

        return scheduled
    }

    override fun notifyNow(title: String, message: String) {
        showNotification(
            context = context,
            title = title,
            message = message
        )
    }
}