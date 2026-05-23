package com.mevi.lasheslam.ui.products.details.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun DetailCharactView(caracteristicas: String) {
    Column {
        Text(
            text = caracteristicas,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Light,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}