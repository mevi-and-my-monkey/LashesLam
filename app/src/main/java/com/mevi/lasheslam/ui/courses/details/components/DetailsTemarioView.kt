package com.mevi.lasheslam.ui.courses.details.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mevi.lasheslam.R
import com.mevi.lasheslam.ui.courses.details.ExpandableSection

@Composable
fun DetailsTemarioView(temarios: List<String>) {
    ExpandableSection(title = stringResource(R.string.temarios_title)) {
        temarios.forEachIndexed { index, temario ->
            if (temario.isNotBlank()) {
                Text(
                    text = "• Día ${index + 1}: $temario",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(4.dp))
            }
        }
    }
}