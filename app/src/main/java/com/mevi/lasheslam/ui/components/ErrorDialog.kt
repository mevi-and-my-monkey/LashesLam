package com.mevi.lasheslam.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.mevi.lasheslam.R

@Composable
fun ErrorDialog(
    onDismiss: () -> Unit,
    onCancel: () -> Unit,
    title: String = stringResource(R.string.error),
    message: String = stringResource(R.string.error_generic_content),
    drawableRes: Int = R.drawable.ic_error
) {
    CustomDialog(
        onDismiss = onDismiss,
        onCancel = onCancel,
        title = title,
        message = message,
        drawableRes = drawableRes,
        backgroundColor = Color(0xFFFFC9C9),
        buttonColor = Color(0xFFF44336)
    )
}