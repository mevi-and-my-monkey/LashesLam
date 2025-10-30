package com.mevi.lasheslam.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.mevi.lasheslam.R

@Composable
fun ErrorDialog(
    onDismiss: () -> Unit,
    onCancel: () -> Unit,
    title: String = "¡Error!",
    message: String = "Ha ocurrido un error inesperado",
    drawableRes: Int = R.drawable.ic_error
) {
    CustomDialog(
        onDismiss = onDismiss,
        onCancel = onCancel,
        title = title,
        message = message,
        drawableRes = drawableRes,
        backgroundColor = Color(0xFFFFC9C9), // rojo pastel
        buttonColor = Color(0xFFF44336)      // rojo más vivo para el botón
    )
}