package com.mevi.lasheslam.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.mevi.lasheslam.R
import com.mevi.lasheslam.ui.theme.LashesLamTheme

@Composable
fun WarningDialog(
    onDismiss: () -> Unit,
    onCancel: () -> Unit,
    title: String = "¡Aviso!",
    message: String = "Estas seguro de realizar esta acción?",
    drawableRes: Int = R.drawable.ic_warning,
    buttonTextOnCancel: String = "Cancelar",

    ) {
    CustomDialog(
        onDismiss = onDismiss,
        title = title,
        message = message,
        drawableRes = drawableRes,
        backgroundColor = Color(0xFFFBF0B9),
        buttonColor = Color(0xFFFF9800),
        buttonTextOnCancel = buttonTextOnCancel,
        onCancel = onCancel
    )
}

@Preview(showBackground = true, name = "LoginLogo")
@Composable
fun WarningDialogPreview() {
    LashesLamTheme {
        WarningDialog({}, {})
    }
}