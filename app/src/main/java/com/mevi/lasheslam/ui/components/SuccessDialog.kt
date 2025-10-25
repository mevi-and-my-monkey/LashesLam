package com.mevi.lasheslam.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.mevi.lasheslam.R

@Composable
fun SuccessDialog(
    onDismiss: () -> Unit,
    title: String = "¡Éxito!",
    message: String = "La operación se completó correctamente",
    drawableRes: Int = R.drawable.ic_success
) {
    CustomDialog(
        onDismiss = onDismiss,
        title = title,
        message = message,
        drawableRes = drawableRes,
        backgroundColor = Color(0xFFB9FBC0), // verde pastel
        buttonColor = Color(0xFF4CAF50)      // verde más vivo para el botón
    )
}

@Composable
fun ErrorDialog(
    onDismiss: () -> Unit,
    title: String = "¡Error!",
    message: String = "Ha ocurrido un error inesperado",
    drawableRes: Int = R.drawable.ic_error
) {
    CustomDialog(
        onDismiss = onDismiss,
        title = title,
        message = message,
        drawableRes = drawableRes,
        backgroundColor = Color(0xFFFFC9C9), // rojo pastel
        buttonColor = Color(0xFFF44336)      // rojo más vivo para el botón
    )
}