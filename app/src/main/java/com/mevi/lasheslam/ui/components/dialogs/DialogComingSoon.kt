package com.mevi.lasheslam.ui.components.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.mevi.lasheslam.R
import com.mevi.lasheslam.ui.components.GenericButton
import com.mevi.lasheslam.ui.theme.LashesLamTheme

@Composable
fun DialogComingSon(
    onDismiss: () -> Unit,
    backgroundColor: Color = Color.White,
    drawableRes: Int,
    title: String,
    content: String,
    textButton: String
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .background(backgroundColor, shape = RoundedCornerShape(16.dp))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                painter = painterResource(drawableRes),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(64.dp)
            )

            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Text(
                text = content,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                fontStyle = FontStyle.Italic,
                lineHeight = 18.sp,
                letterSpacing = 0.5.sp,
                textAlign = TextAlign.Center,
                color = Color.Black.copy(.5f)
            )

            GenericButton(
                text = textButton,
                onClick = { onDismiss() },
                backgroundColor = MaterialTheme.colorScheme.primary,
                textColor = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Preview(showBackground = true, name = "LoginLogo")
@Composable
fun DialogComingSonPreview() {
    LashesLamTheme {
        DialogComingSon(
            onDismiss = {},
            drawableRes = R.drawable.ic_star,
            title = stringResource(R.string.title_coming_soon),
            content = stringResource(R.string.content_coming_soon),
            textButton = stringResource(R.string.button_understand)
        )
    }
}
