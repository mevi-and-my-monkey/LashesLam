package com.mevi.lasheslam.ui.courses.edit.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mevi.lasheslam.R
import com.mevi.lasheslam.network.LocationItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditLocationsView(
    locations: List<LocationItem>,
    expanded: Boolean,
    selectedLocation: LocationItem?,
    onExpandedChange: (Boolean) -> Unit,
    onLocationSelected: (LocationItem) -> Unit
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { onExpandedChange(it) }
    ) {
        OutlinedTextField(
            value = selectedLocation?.name ?: "Seleccionar ubicación",
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(R.string.location)) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                onExpandedChange(!expanded)
            }
        ) {
            locations.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item.name) },
                    onClick = {
                        onLocationSelected(item)
                        onExpandedChange(false)
                    }
                )
            }
        }
    }
}