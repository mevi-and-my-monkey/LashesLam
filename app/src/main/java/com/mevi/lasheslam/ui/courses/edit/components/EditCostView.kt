package com.mevi.lasheslam.ui.courses.edit.components

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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.mevi.lasheslam.R

@Composable
fun EditCostView(
    cost: String,
    onCostChange: (String) -> Unit,
    apartar: String,
    onApartaChange: (String) -> Unit
) {
    OutlinedTextField(
        value = cost,
        onValueChange = { newValue ->
            if (newValue.matches(Regex("""\d*\.?\d*"""))) {
                onCostChange(newValue)
            }
        },
        label = { Text(stringResource(R.string.costo)) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        shape = RoundedCornerShape(12.dp)
    )
    Spacer(modifier = Modifier.height(8.dp))

    OutlinedTextField(
        value = apartar,
        onValueChange = { newValue ->
            if (newValue.matches(Regex("""\d*\.?\d*"""))) {
                onApartaChange(newValue)
            }
        },
        label = { Text(stringResource(R.string.apartado)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
    )
}