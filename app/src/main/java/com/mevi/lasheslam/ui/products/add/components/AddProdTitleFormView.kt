package com.mevi.lasheslam.ui.products.add.components

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
import com.mevi.lasheslam.ui.products.ProductsUiState

@Composable
fun AddProdTitleFormView(
    state: ProductsUiState,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit
){
    OutlinedTextField(
        value = state.form.titulo,
        onValueChange = { onTitleChange(it) },
        label = { Text(stringResource(R.string.title)) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            imeAction = ImeAction.Next
        ),
        shape = RoundedCornerShape(12.dp)
    )
    Spacer(modifier = Modifier.height(8.dp))

    OutlinedTextField(
        value = state.form.descripcion,
        onValueChange = { onDescriptionChange(it) },
        label = { Text(stringResource(R.string.description)) },
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
}