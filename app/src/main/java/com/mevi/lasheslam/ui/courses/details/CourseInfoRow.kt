package com.mevi.lasheslam.ui.courses.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mevi.lasheslam.R

@Composable
fun CourseInfoRow(
    fecha: String,
    horario: String,
    onLocationClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        InfoIcon(icon = Icons.Default.CalendarToday, text = fecha)
        InfoIcon(icon = Icons.Default.Schedule, text = horario)

        TextButton(onClick = onLocationClick) {
            Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color(0xFFD9869A))
            Spacer(Modifier.width(4.dp))
            Text(
                stringResource(R.string.ubicacion),
                color = Color(0xFFD9869A),
                fontWeight = FontWeight.Bold
            )
        }
    }
}