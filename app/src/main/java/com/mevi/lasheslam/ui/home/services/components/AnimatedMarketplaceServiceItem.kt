package com.mevi.lasheslam.ui.home.services.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.mevi.lasheslam.domain.analytics.AnalyticsEvent
import com.mevi.lasheslam.network.ServiceItem

@Composable
fun AnimatedMarketplaceServiceItem(
    services: ServiceItem,
    index: Int,
    onClick: () -> Unit = {},
    trackEvent: (AnalyticsEvent) -> Unit
) {
    val alpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 400, delayMillis = index * 50)
    )

    val offsetY by animateDpAsState(
        targetValue = 0.dp,
        animationSpec = tween(durationMillis = 400, delayMillis = index * 50)
    )

    ServiceItemView(
        trackEvent = trackEvent,
        modifier = Modifier
            .padding(vertical = 8.dp)
            .graphicsLayer {
                this.alpha = alpha
                translationY = offsetY.toPx()
            },
        service = services,
        onClick = onClick
    )

}