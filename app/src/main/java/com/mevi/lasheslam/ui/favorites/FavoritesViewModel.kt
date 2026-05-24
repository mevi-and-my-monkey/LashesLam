package com.mevi.lasheslam.ui.favorites

import com.mevi.lasheslam.BaseViewModel
import com.mevi.lasheslam.domain.analytics.AnalyticsEvent
import com.mevi.lasheslam.domain.repository.AnalyticsTracker
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val analytics: AnalyticsTracker,
) : BaseViewModel<FavoritesUiState, FavoriteUiEvent>() {

    override fun createInitialState() = FavoritesUiState()

    fun trackEvent(event: AnalyticsEvent) {
        analytics.track(event)
    }

    fun trackScreen(screen: String) {
        analytics.track(AnalyticsEvent.ScreenView(screen))
    }
}