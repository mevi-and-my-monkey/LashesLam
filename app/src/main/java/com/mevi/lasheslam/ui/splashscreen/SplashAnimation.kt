package com.mevi.lasheslam.ui.splashscreen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Animatable
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
    offsetX: Animatable<Float, *>,
    offsetY: Animatable<Float, *>
) {
    val infiniteTransition = rememberInfiniteTransition()
    val shimmerShift by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(tween(2500, easing = LinearEasing), RepeatMode.Restart)
    )

    val brush = Brush.linearGradient(
        colors = listOf(Color(0xFFFFC1E3), Color(0xFFFF69B4), Color(0xFFFFC1E3)),
        start = Offset(shimmerShift, 0f),
        end = Offset(shimmerShift + 200f, 200f)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A)),
        contentAlignment = Alignment.Center
    ) {
        if (!showFullName) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "L",
                    fontSize = 60.sp,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(brush = brush),
                    modifier = Modifier.offset(x = offsetX.value.dp)
                )
                Text(
                    "L",
                    fontSize = 60.sp,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(brush = brush),
                    modifier = Modifier.offset(x = offsetY.value.dp)
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

        Column(modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = 20.dp)) {
            Text(
                text = "Version : ${getVersion()}:(${getVersionCode()})",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFC1E3)
            )
        }

    }
}