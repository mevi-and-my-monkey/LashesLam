package com.mevi.lasheslam.data

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.mevi.lasheslam.domain.analytics.AnalyticsEvent
import com.mevi.lasheslam.domain.analytics.AnalyticsParams
import com.mevi.lasheslam.domain.repository.AnalyticsTracker
import javax.inject.Inject

class FirebaseAnalyticsTracker @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics
) : AnalyticsTracker {

    override fun track(event: AnalyticsEvent) {
        val bundle = mapParams(event)
        firebaseAnalytics.logEvent(event.name, bundle)
    }

    override fun setUserId(userId: String?) {
        firebaseAnalytics.setUserId(userId)
    }

    private fun mapParams(event: AnalyticsEvent): Bundle {
        return Bundle().apply {
            when (event) {

                is AnalyticsEvent.LoginSuccess -> {
                    putString(AnalyticsParams.METHOD, event.method)
                }

                is AnalyticsEvent.LoginError -> {
                    putString(AnalyticsParams.ERROR, event.message)
                }

                is AnalyticsEvent.ScreenView -> {
                    putString(AnalyticsParams.SCREEN_NAME, event.screen)
                }

                is AnalyticsEvent.SectionSelected -> {
                    putString(AnalyticsParams.SECTION, event.section)
                }

                is AnalyticsEvent.BottomSelection -> {
                    putString(AnalyticsParams.SECTION, event.section)
                }

                else -> Unit
            }
        }
    }
}