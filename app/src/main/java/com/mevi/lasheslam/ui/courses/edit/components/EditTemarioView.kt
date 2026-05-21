package com.mevi.lasheslam.ui.courses.edit.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.mevi.lasheslam.R
import com.mevi.lasheslam.ui.courses.details.ExpandableSection

@Composable
fun EditTemarioView(temarios: List<String>, onTemarioChange: (Int, String) -> Unit) {
    val fixedTemarios = List(5) { index -> temarios.getOrNull(index) ?: "" }
    ExpandableSection(title = stringResource(R.string.temarios_title)) {
        fixedTemarios.forEachIndexed { index, temario ->
            OutlinedTextField(
                value = temario,
                onValueChange = { onTemarioChange(index, it) },
                label = { Text("Día ${index + 1}") },
                placeholder = { Text("Contenido del día ${index + 1}") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next
                ),
                shape = RoundedCornerShape(12.dp)
            )
        }
    }
}