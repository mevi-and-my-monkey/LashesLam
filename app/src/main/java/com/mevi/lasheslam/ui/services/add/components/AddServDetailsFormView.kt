package com.mevi.lasheslam.ui.services.add.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.mevi.lasheslam.R
import com.mevi.lasheslam.ui.services.ServiceUiState

@Composable
fun AddServDetailsFormView(
    state: ServiceUiState,
    onDescriptionChange: (String) -> Unit,
    onIncludesChange: (String) -> Unit
) {
    OutlinedTextField(
        value = state.form.descripcion,
        onValueChange = { onDescriptionChange(it) },
        label = { Text(stringResource(R.string.service_description)) },
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 100.dp),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        shape = RoundedCornerShape(12.dp)
    )
    Spacer(modifier = Modifier.height(8.dp))

    OutlinedTextField(
        value = state.form.incluye,
        onValueChange = { onIncludesChange(it) },
        label = { Text(stringResource(R.string.service_includes)) },
        supportingText = { Text(stringResource(R.string.service_includes_hint)) },
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 120.dp),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Default
        ),
        shape = RoundedCornerShape(12.dp)
    )
    Spacer(modifier = Modifier.height(8.dp))
}
