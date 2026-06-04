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
        firebaseAnalytics.logEvent(event.name, event.toBundle())
    }

    override fun setUserId(userId: String?) {
        firebaseAnalytics.setUserId(userId)
    }

    private fun AnalyticsEvent.toBundle(): Bundle {
        return Bundle().apply {
            when (this@toBundle) {
                is AnalyticsEvent.LoginSuccess -> {
                    putString(AnalyticsParams.METHOD, method)
                }

                is AnalyticsEvent.LoginError -> {
                    putString(AnalyticsParams.ERROR, message)
                }

                is AnalyticsEvent.ScreenView -> {
                    putString(AnalyticsParams.SCREEN_NAME, screen)
                }

                is AnalyticsEvent.SectionSelected -> {
                    putString(AnalyticsParams.SECTION, section)
                }

                is AnalyticsEvent.BottomSelection -> {
                    putString(AnalyticsParams.SECTION, section)
                }

                else -> Unit
            }
        }
    }
}