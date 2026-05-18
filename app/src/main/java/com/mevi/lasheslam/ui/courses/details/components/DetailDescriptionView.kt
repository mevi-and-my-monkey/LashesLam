package com.mevi.lasheslam.ui.courses.details.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mevi.lasheslam.R

@Composable
fun DetailDescriptionView(descripcion: String) {
    Text(
        text = stringResource(R.string.about_course),
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp
    )

    Spacer(Modifier.height(16.dp))

    Text(
        text = descripcion,
        style = MaterialTheme.typography.bodyLarge
    )
}