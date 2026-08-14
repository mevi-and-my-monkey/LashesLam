package com.mevi.lasheslam.data.local

import com.mevi.lasheslam.domain.repository.NotificationCache
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationCacheImpl @Inject constructor() : NotificationCache {
    private val processed = mutableSetOf<String>()
    override fun shouldProcess(id: String): Boolean {
        return processed.add(id)
    }
}
