package com.mevi.lasheslam.ui.splashscreen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mevi.lasheslam.ui.splashscreen.components.getVersion
import com.mevi.lasheslam.ui.splashscreen.components.getVersionCode

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SplashAnimation(
    showFullName: Boolean,
    visibleText: String,
    offsetX: Float,
    offsetY: Float,
    modifier: Modifier
) {
    val context = LocalContext.current

    val versionName = remember { getVersion(context) }
    val versionCode = remember { getVersionCode(context) }

    val infiniteTransition = rememberInfiniteTransition()
    val shimmerShift by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(tween(2500, easing = LinearEasing), RepeatMode.Restart)
    )

    val brush = remember(shimmerShift) {
        Brush.linearGradient(
            colors = listOf(SplashColors.Pink, Color(0xFFFF69B4), SplashColors.Pink),
            start = Offset(shimmerShift, 0f),
            end = Offset(shimmerShift + 200f, 200f)
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SplashColors.Background),
        contentAlignment = Alignment.Center
    ) {
        if (!showFullName) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "L",
                    fontSize = 60.sp,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(brush = brush),
                    modifier = Modifier.offset(x = offsetX.dp)
                )
                Text(
                    "L",
                    fontSize = 60.sp,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(brush = brush),
                    modifier = Modifier.offset(x = offsetY.dp)
                )
            }
        } else {
            AnimatedContent(
                targetState = visibleText,
                transitionSpec = { fadeIn(tween(200)).togetherWith(fadeOut(tween(200))) }) { text ->
                Text(
                    text,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(brush = brush)
                )
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp)
        ) {
            Text(
                text = "Version: $versionName ($versionCode)",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = SplashColors.Pink
            )
        }

    }
}