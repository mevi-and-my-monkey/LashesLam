package com.mevi.lasheslam.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.tooling.preview.Preview
import com.mevi.lasheslam.ui.theme.LashesLamTheme

@Composable
fun WavyBackground(
    modifier: Modifier = Modifier.Companion,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    bigWaveColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    smallWaveColor: Color = MaterialTheme.colorScheme.surface,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(if (isSystemInDarkTheme()) Color.Black else backgroundColor),
        contentAlignment = Alignment.TopCenter
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            // Ola grande (fondo)
            val bigWave = Path().apply {
                moveTo(0f, canvasHeight * 0.28f)
                quadraticBezierTo(
                    x1 = canvasWidth / 2,
                    y1 = canvasHeight * 0.43f,
                    x2 = canvasWidth,
                    y2 = canvasHeight * 0.28f
                )
                lineTo(canvasWidth, canvasHeight)
                lineTo(0f, canvasHeight)
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
                lineTo(canvasWidth, canvasHeight)
                lineTo(0f, canvasHeight)
                close()
            }

            // Dibuja ambas olas
            drawPath(path = bigWave, color = bigWaveColor) // tono más claro
            drawPath(path = smallWave, color = smallWaveColor)
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