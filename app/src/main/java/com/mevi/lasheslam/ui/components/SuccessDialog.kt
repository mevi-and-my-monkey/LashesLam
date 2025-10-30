package com.mevi.lasheslam.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.mevi.lasheslam.R

@Composable
fun SuccessDialog(
    onDismiss: () -> Unit,
    onCancel: () -> Unit,
    title: String = "¡Éxito!",
    message: String = "La operación se completó correctamente",
    drawableRes: Int = R.drawable.ic_success
) {
    CustomDialog(
        onDismiss = onDismiss,
        onCancel = onCancel,
        title = title,
        message = message,
        drawableRes = drawableRes,
        backgroundColor = Color(0xFFB9FBC0), // verde pastel
        buttonColor = Color(0xFF4CAF50)      // verde más vivo para el botón
    )
}