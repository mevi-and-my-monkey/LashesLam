package com.mevi.lasheslam.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mevi.lasheslam.R
import com.mevi.lasheslam.core.Strings
import com.mevi.lasheslam.ui.theme.LashesLamTheme

@Composable
fun GenericIconButton(
    icon: Painter,
    contentDescription: String? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier.Companion,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    iconTint: Color = Color.Companion.Unspecified, // deja el PNG con su color original
    size: Int = 56 // tamaño más grande tipo FAB
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .size(size.dp),
        shape = CircleShape, // 👈 redondo
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = Color.Companion.Unspecified // 👈 no tintar el icono PNG
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 6.dp,
            pressedElevation = 8.dp
        ),
        contentPadding = PaddingValues(0.dp) // 👈 centra mejor el icono
    ) {
        Icon(
            painter = icon,
            contentDescription = contentDescription,
            tint = iconTint,
            modifier = Modifier.Companion.size((size * 0.55f).dp) // icono proporcional
        )
    }
}

@Preview(showBackground = true, showSystemUi = false, name = "Round Icon Button")
@Composable
fun GenericIconButtonPreview() {
    LashesLamTheme {
        GenericIconButton(
            icon = painterResource(id = R.drawable.ic_google_one),
            contentDescription = Strings.loginWithGoogle,
            onClick = { /* Acción */ },
            backgroundColor = Color.Companion.White,
            iconTint = Color.Companion.Unspecified // respeta los colores del PNG
        )
    }
}