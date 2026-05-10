package com.mevi.lasheslam.ui.courses.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.mevi.lasheslam.ui.home.cursos.CourseUiState

@Composable
fun AddDateFormView(state: CourseUiState, onShowDatePicker: () -> Unit) {
    OutlinedTextField(
        value = state.form.fecha,
        onValueChange = { },
        readOnly = true,
        label = { Text(stringResource(R.string.fecha)) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        trailingIcon = {
            IconButton(onClick = onShowDatePicker) {
                Icon(Icons.Default.CalendarMonth, contentDescription = null)
            }
        }
    )
    Spacer(modifier = Modifier.height(8.dp))
}