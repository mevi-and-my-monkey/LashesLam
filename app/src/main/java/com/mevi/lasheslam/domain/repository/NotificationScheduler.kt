package com.mevi.lasheslam.domain.repository

import java.time.LocalDateTime

interface NotificationScheduler {
    fun schedule(
        courseId: String,
        courseName: String,
        startDateTime: LocalDateTime
    ): Boolean
    fun notifyNow(title: String, message: String)
}
