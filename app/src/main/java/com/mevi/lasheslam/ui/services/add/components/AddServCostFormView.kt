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
    val costoFormateado = remember(state.form.precio) {
        NumberFormat.getCurrencyInstance(
            Locale(
                Constants.Project.LANGUAGE,
                Constants.Project.COUNTRY
            )
        ).format(state.form.precio)
    }

    OutlinedTextField(
        value = state.form.precio,
        onValueChange = onCostChange,
        label = { Text(stringResource(R.string.costo)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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

    OutlinedTextField(
        value = state.form.duracion,
        onValueChange = onDurationChange,
        label = { Text(stringResource(R.string.duration)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
    )
    Spacer(modifier = Modifier.height(8.dp))
}