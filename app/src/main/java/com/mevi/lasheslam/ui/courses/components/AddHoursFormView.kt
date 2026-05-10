package com.mevi.lasheslam.ui.courses.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mevi.lasheslam.R
import com.mevi.lasheslam.ui.home.cursos.CourseUiState

@Composable
fun AddHoursFormView(
    state: CourseUiState,
    onShowTimePickerInicio: () -> Unit,
    onShowTimePickerFin: () -> Unit
) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = state.form.horaInicio,
            onValueChange = { },
            readOnly = true,
            label = { Text(stringResource(R.string.hora_inicio)) },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp),
            trailingIcon = {
                IconButton(onClick = onShowTimePickerInicio) {
                    Icon(Icons.Default.AccessTime, contentDescription = null)
                }
            }
        )
        OutlinedTextField(
            value = state.form.horaFin,
            onValueChange = { },
            readOnly = true,
            label = { Text(stringResource(R.string.hora_fin)) },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp),
            trailingIcon = {
                IconButton(onClick = onShowTimePickerFin) {
                    Icon(Icons.Default.AccessTime, contentDescription = null)
                }
            }
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
}