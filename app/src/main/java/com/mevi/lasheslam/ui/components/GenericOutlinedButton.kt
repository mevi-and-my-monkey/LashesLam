package com.mevi.lasheslam.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mevi.lasheslam.ui.theme.LashesLamTheme

@Composable
fun GenericOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier.Companion,
    textColor: Color = MaterialTheme.colorScheme.onBackground,
    borderColor: Color = MaterialTheme.colorScheme.outline,
    borderWidth: Dp = 1.dp,
    icon: Painter? = null
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth(),
        border = BorderStroke(borderWidth, borderColor),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = textColor,
            containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.1f)
        ),

        ) {
        if (icon != null) {
            Icon(
                painter = icon,
                contentDescription = null,
                tint = Color.Companion.Unspecified,
                modifier = Modifier.Companion
                    .size(20.dp)
                    .padding(end = 8.dp)
            )
        }
        Text(
            text = text,
            color = textColor,
            maxLines = 1,
            overflow = TextOverflow.Companion.Ellipsis,
            fontSize = 16.sp,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Preview(showBackground = true, showSystemUi = false, name = "General Button")
@Composable
fun GenericOutlinedButtonPreview() {
    LashesLamTheme {
        GenericOutlinedButton(
            text = "Registrarse",
            onClick = { /* Acción Registro */ },
            textColor = Color.Companion.Gray,
            borderColor = Color.Companion.Gray
        )
    }
}