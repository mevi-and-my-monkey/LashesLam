package com.mevi.lasheslam.data.local

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationCache @Inject constructor() {
    private val processed = mutableSetOf<String>()
    fun shouldProcess(courseId: String): Boolean {
        return processed.add(courseId)
    }
}