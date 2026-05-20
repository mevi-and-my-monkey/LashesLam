package com.mevi.lasheslam.ui.courses.edit.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mevi.lasheslam.R

@Composable
fun EditScheduleView(
    hourStart: String,
    hourEnd: String,
    date: String,
    showDatePicker: () -> Unit,
    showTimePickerInicio: () -> Unit,
    showTimePickerFin: () -> Unit
) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = hourStart,
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(R.string.hora_inicio)) },
            modifier = Modifier.weight(1f),
            trailingIcon = {
                IconButton(onClick = showTimePickerInicio) {
                    Icon(Icons.Default.AccessTime, contentDescription = null)
                }
            }
        )

        OutlinedTextField(
            value = hourEnd,
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(R.string.hora_fin)) },
            modifier = Modifier.weight(1f),
            trailingIcon = {
                IconButton(onClick = showTimePickerFin) {
                    Icon(Icons.Default.AccessTime, contentDescription = null)
                }
            }
        )
    }
    Spacer(modifier = Modifier.height(8.dp))

    OutlinedTextField(
        value = date,
        onValueChange = {},
        readOnly = true,
        label = { Text(stringResource(R.string.fecha)) },
        modifier = Modifier.fillMaxWidth(),
        trailingIcon = {
            IconButton(onClick = showDatePicker) {
                Icon(Icons.Default.CalendarMonth, contentDescription = null)
            }
        }
    )
}