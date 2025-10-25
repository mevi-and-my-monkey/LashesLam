package com.mevi.lasheslam.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mevi.lasheslam.ui.theme.LashesLamTheme

@Composable
fun GenericButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier.Companion,
    backgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
    textColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    icon: ImageVector? = null,
    enabled: Boolean? = true
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = textColor
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
        enabled = enabled ?: true
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = textColor,
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
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Companion.SemiBold
            )
        )
    }
}

@Preview(showBackground = true, showSystemUi = false, name = "General Button")
@Composable
fun GenericButtonPreview() {
    LashesLamTheme {
        GenericButton(
            "Login", onClick = {}, backgroundColor = MaterialTheme.colorScheme.primary,
            textColor = MaterialTheme.colorScheme.onPrimary
        )
    }
}