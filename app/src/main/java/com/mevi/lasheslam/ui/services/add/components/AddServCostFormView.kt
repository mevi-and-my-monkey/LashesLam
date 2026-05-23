package com.mevi.lasheslam.ui.services.add.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.mevi.lasheslam.R
import com.mevi.lasheslam.ui.services.ServiceUiState
import com.mevi.lasheslam.utils.Constants
import java.text.NumberFormat
import java.util.Locale

@Composable
fun AddServCostFormView(
    state: ServiceUiState,
    onCostChange: (String) -> Unit,
    onDurationChange: (String) -> Unit,
) {
    val precioDouble = remember(state.form.precio) { state.form.precio.toDoubleOrNull() }

    val costoFormateado = remember(precioDouble) {
        precioDouble?.let {
            NumberFormat.getCurrencyInstance(
                Locale(
                    Constants.Project.LANGUAGE,
                    Constants.Project.COUNTRY
                )
            ).format(it)
        } ?: ""
    }

    OutlinedTextField(
        value = state.form.precio,
        onValueChange = onCostChange,
        label = { Text(stringResource(R.string.costo)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
    )

    if (costoFormateado.isNotEmpty()) {
        Text(
            text = "${stringResource(R.string.moneda)} $costoFormateado",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
    Spacer(modifier = Modifier.height(8.dp))

    OutlinedTextField(
        value = state.form.duracion,
        onValueChange = onDurationChange,
        label = { Text(stringResource(R.string.duration)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
    )

    Spacer(modifier = Modifier.height(8.dp))
}