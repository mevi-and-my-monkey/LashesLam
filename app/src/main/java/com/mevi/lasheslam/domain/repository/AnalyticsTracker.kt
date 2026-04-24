package com.mevi.lasheslam.domain.repository

import com.mevi.lasheslam.domain.analytics.AnalyticsEvent

interface AnalyticsTracker {
    fun track(event: AnalyticsEvent)
    fun setUserId(userId: String?)
}