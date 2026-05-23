package com.mevi.lasheslam.ui.products.add.components

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
import com.mevi.lasheslam.ui.products.ProductsUiState
import com.mevi.lasheslam.utils.Constants
import java.text.NumberFormat
import java.util.Locale

@Composable
fun AddProdCostFormView(
    state: ProductsUiState,
    onCostChange: (String) -> Unit,
    onActualCostChange: (String) -> Unit
) {
    val numberFormat = remember {
        NumberFormat.getCurrencyInstance(
            Locale(
                Constants.Project.LANGUAGE,
                Constants.Project.COUNTRY
            )
        )
    }

    val costoFormateado = remember(state.form.precio) {
        state.form.precio
            .toDoubleOrNull()
            ?.let { numberFormat.format(it) }
            ?: ""
    }

    val costoActualFormateado = remember(state.form.precioActual) {
        state.form.precioActual
            .toDoubleOrNull()
            ?.let { numberFormat.format(it) }
            ?: ""
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
    Spacer(modifier = Modifier.height(8.dp))

    OutlinedTextField(
        value = state.form.precioActual,
        onValueChange = onActualCostChange,
        label = { Text(stringResource(R.string.actual_price)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
    )

    if (costoActualFormateado.isNotEmpty()) {
        Text(
            text = "${stringResource(R.string.moneda)} $costoActualFormateado",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
}