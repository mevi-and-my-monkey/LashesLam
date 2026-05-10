package com.mevi.lasheslam.ui.courses.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.mevi.lasheslam.ui.home.cursos.CourseUiState

@Composable
fun AddTemariosFormView(state: CourseUiState, onTemarioChange: (Int, String) -> Unit) {
    state.form.temarios.forEachIndexed { index, temario ->
        OutlinedTextField(
            value = temario,
            onValueChange = {
                onTemarioChange(index, it)
            },
            label = {
                Text("${stringResource(R.string.temario)} ${index + 1}")
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next
            ),
            shape = RoundedCornerShape(12.dp)
        )
    }

    Spacer(Modifier.height(24.dp))
}