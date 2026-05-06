package com.mevi.lasheslam.ui.home.cursos.components

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
import com.mevi.lasheslam.network.CoursesItem

@Composable
fun AnimatedMarketplaceItem(
    trackEvent: (AnalyticsEvent) -> Unit,
    courses: CoursesItem,
    index: Int,
    favorites: Set<String>,
    onToggleFavorite: (String) -> Unit,
    onClick: () -> Unit = {},
) {
    val alpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 400, delayMillis = index * 50)
    )

    val offsetY by animateDpAsState(
        targetValue = 0.dp,
        animationSpec = tween(durationMillis = 400, delayMillis = index * 50)
    )

    CursesItem(
        trackEvent = trackEvent,
        modifier = Modifier
            .padding(vertical = 8.dp)
            .graphicsLayer {
                this.alpha = alpha
                translationY = offsetY.toPx()
            },
        courses = courses,
        isFavorite = favorites.contains(courses.id),
        onToggleFavorite = { onToggleFavorite(courses.id) },
        onClick = onClick
    )
}