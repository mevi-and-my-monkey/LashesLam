package com.mevi.lasheslam.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mevi.lasheslam.Strings
import kotlinx.coroutines.delay

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SplashScreen(navController: NavController) {
    var showFullName by remember { mutableStateOf(false) }
    var visibleText by remember { mutableStateOf("") }
    val fullText = Strings.appName

    val offsetX = remember { Animatable(-200f) }
    val offsetY = remember { Animatable(200f) }

    // Degradado animado tipo "brillo rosa"
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val shimmerShift by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            tween(durationMillis = 2500, easing = LinearEasing),
            RepeatMode.Restart
        ),
        label = ""
    )

    val brush = Brush.linearGradient(
        colors = listOf(
            Color(0xFFFFC1E3), // rosa claro
            Color(0xFFFF69B4), // rosa intenso
            Color(0xFFFFC1E3)  // vuelve al claro
        ),
        start = Offset(shimmerShift, 0f),
        end = Offset(shimmerShift + 200f, 200f)
    )

    LaunchedEffect(Unit) {
        // Animación inicial de las dos "L"
        offsetX.animateTo(0f, tween(800, easing = LinearOutSlowInEasing))
        offsetY.animateTo(0f, tween(800, easing = LinearOutSlowInEasing))

        delay(300)
        showFullName = true

        // Animar escritura de cada letra
        fullText.forEachIndexed { index, _ ->
            visibleText = fullText.substring(0, index + 1)
            delay(100)
        }

        delay(1200)
        navController.navigate("login") {
            popUpTo("splash") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A)),
            //.background(Color(0xFFFFFFFF)),
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
                transitionSpec = { fadeIn(tween(200)) with fadeOut(tween(200)) },
                label = ""
            ) { text ->
                Text(
                    text = text,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(brush = brush),
                )
            }
        }
    }
}