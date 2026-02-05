package com.mevi.lasheslam.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.tooling.preview.Preview
import com.mevi.lasheslam.ui.theme.LashesLamTheme

@Composable
fun WavyBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val backgroundColor = Color(0xFFFFFBFB)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(if (isSystemInDarkTheme()) Color.Black else backgroundColor),
        contentAlignment = Alignment.TopCenter
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            val bigWaveGradient = Brush.verticalGradient(
                colors = listOf(Color(0xFFFEF0F2), Color(0xFFFADADD)),
                startY = 0f,
                endY = canvasHeight * 0.43f
            )
            val smallWaveGradient = Brush.verticalGradient(
                colors = listOf(Color(0xFFFADADD), Color(0xFFE8A0B2)),
                startY = 0f,
                endY = canvasHeight * 0.45f
            )

            // Ola grande (fondo)
            val bigWave = Path().apply {
                moveTo(0f, canvasHeight * 0.28f)
                quadraticBezierTo(
                    x1 = canvasWidth / 2,
                    y1 = canvasHeight * 0.43f,
                    x2 = canvasWidth,
                    y2 = canvasHeight * 0.28f
                )
                lineTo(canvasWidth, 0f)
                lineTo(0f, 0f)
                close()
            }

            // Ola principal (frontal)
            val smallWave = Path().apply {
                moveTo(0f, canvasHeight * 0.3f)
                quadraticBezierTo(
                    x1 = canvasWidth / 2,
                    y1 = canvasHeight * 0.45f,
                    x2 = canvasWidth,
                    y2 = canvasHeight * 0.3f
                )
                lineTo(canvasWidth, 0f)
                lineTo(0f, 0f)
                close()
            }

            // Dibuja ambas olas
            drawPath(path = bigWave, brush = bigWaveGradient)
            drawPath(path = smallWave, brush = smallWaveGradient)
        }
        content()
    }
}

@Preview(name = "Phone", widthDp = 360, heightDp = 800, showBackground = true)
@Composable
fun WavyBackgroundPhonePreview() {
    LashesLamTheme {
        WavyBackground { }
    }
}

@Preview(name = "Tablet", widthDp = 1280, heightDp = 800, showBackground = true)
@Composable
fun WavyBackgroundTabletPreview() {
    LashesLamTheme {
        WavyBackground { }
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Wavy BG")
@Composable
fun WavyBackgroundPreview() {
    LashesLamTheme {
        WavyBackground {}
    }
}