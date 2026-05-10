package com.mevi.lasheslam.ui.courses.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AddTitleView(title: String){
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge
    )
    Spacer(Modifier.height(16.dp))
}