package com.mevi.lasheslam.ui.services

import com.mevi.lasheslam.BaseViewModel
import com.mevi.lasheslam.domain.analytics.AnalyticsEvent
import com.mevi.lasheslam.domain.repository.AnalyticsTracker
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ServicesViewModel @Inject constructor(
    private val analytics: AnalyticsTracker
) : BaseViewModel<ServiceUiState, ServiceUiEvent>() {

    override fun createInitialState() = ServiceUiState()

    fun trackEvent(event: AnalyticsEvent) {
        analytics.track(event)
    }

    fun trackScreen(screen: String) {
        analytics.track(AnalyticsEvent.ScreenView(screen))
    }
}