package com.mevi.lasheslam.ui.courses.components

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
import com.mevi.lasheslam.ui.home.cursos.CourseUiState
import com.mevi.lasheslam.utils.Constants
import java.text.NumberFormat
import java.util.Locale

@Composable
fun AddCostFormView(
    state: CourseUiState,
    onCostChange: (String) -> Unit,
    onApartaChange: (String) -> Unit
) {
    val costoFormateado = remember(state.form.costo) {
        state.form.costo.toDoubleOrNull()?.let {
            NumberFormat.getCurrencyInstance(
                Locale(
                    Constants.Project.LANGUAGE,
                    Constants.Project.COUNTRY
                )
            ).format(it)
        } ?: ""
    }

    val apartadpFormateado = remember(state.form.apartar) {
        state.form.apartar.toDoubleOrNull()?.let {
            NumberFormat.getCurrencyInstance(
                Locale(
                    Constants.Project.LANGUAGE,
                    Constants.Project.COUNTRY
                )
            ).format(it)
        } ?: ""
    }

    OutlinedTextField(
        value = state.form.costo,
        onValueChange = { cost ->
            onCostChange(cost)
        },
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
        value = state.form.apartar,
        onValueChange = { newValue ->
            onApartaChange(newValue)
        },
        label = { Text(stringResource(R.string.apartado)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
    )

    if (apartadpFormateado.isNotEmpty()) {
        Text(
            text = "${stringResource(R.string.moneda)} $apartadpFormateado",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
}