package com.mevi.lasheslam.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mevi.lasheslam.R
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
            .fillMaxWidth()
            .height(52.dp),
        border = BorderStroke(borderWidth, borderColor),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = textColor,
            containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.1f)
        ),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            if (icon != null) {
                Icon(
                    painter = icon,
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.CenterStart)
                )
            }

            Text(
                text = text,
                modifier = Modifier.align(Alignment.Center),
                color = textColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 16.sp,
                style = MaterialTheme.typography.labelLarge
            )
        }
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