package com.mevi.lasheslam.ui.courses.edit.components

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
import androidx.compose.ui.unit.dp
import com.mevi.lasheslam.R

@Composable
fun EditTitleView(
    title: String,
    onTitleChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit
) {
    OutlinedTextField(
        value = title,
        onValueChange = onTitleChange,
        label = { Text(stringResource(R.string.title)) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            imeAction = ImeAction.Next
        ),
    )
    Spacer(modifier = Modifier.height(8.dp))

    OutlinedTextField(
        value = description,
        onValueChange = onDescriptionChange,
        label = { Text(stringResource(R.string.description)) },
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 100.dp),
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            imeAction = ImeAction.Next
        )
    )
}