package com.mevi.lasheslam.ui.courses.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.mevi.lasheslam.R

@Composable
fun CourseContent(
    titulo: String,
    descripcion: String,
    costoTotal: String?,
    costoApartado: String?
) {
    Column {
        Text(
            text = titulo.uppercase(),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(8.dp))

        Text(
            buildAnnotatedString {
                append(stringResource(R.string.total_cost))
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("$${costoTotal ?: "0.0"} MXN")
                }
                if (!costoApartado.isNullOrEmpty()) {
                    append(" / ${stringResource(R.string.seat_aside_with)}")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("$${costoApartado} MXN")
                    }
                }
            },
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = descripcion,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}