package com.mevi.lasheslam.ui.home.cursos.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.mevi.lasheslam.network.CoursesItem

@Composable
fun AnimatedMarketplaceItemSearch(
    service: CoursesItem,
    index: Int,
    onClick: () -> Unit = {}
) {
    val alpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(400, delayMillis = index * 50)
    )

    CursesItemSearch(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .graphicsLayer { this.alpha = alpha },
        service = service,
        onClick = onClick
    )
}