package com.mevi.lasheslam.ui.home.cursos.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mevi.lasheslam.network.ServiceItem
import kotlinx.coroutines.delay

@Composable
fun AnimatedMarketplaceItem(
    service: ServiceItem,
    index: Int,
    onClick: () -> Unit = {}
) {
    val animationDelay = 50 * index

    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(animationDelay.toLong())
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(400)) + slideInVertically(tween(400)) { it / 3 }
    ) {
        CursesItem(modifier = Modifier.padding(vertical = 8.dp), service, onClick)
    }
}