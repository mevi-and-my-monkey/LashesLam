package com.mevi.lasheslam.ui.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAddressBottomSheet(
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var street by remember { mutableStateOf("") }
    var extNumber by remember { mutableStateOf("") }
    var intNumber by remember { mutableStateOf("") }
    var suburb by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var postalCode by remember { mutableStateOf("") }

    val isValid = street.isNotBlank() && extNumber.isNotBlank() && city.isNotBlank() && postalCode.isNotBlank() && suburb.isNotBlank()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Editar dirección", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(value = street, onValueChange = { street = it }, label = { Text("Calle") })
            OutlinedTextField(value = extNumber, onValueChange = { extNumber = it }, label = { Text("Número exterior") })
            OutlinedTextField(value = intNumber, onValueChange = { intNumber = it }, label = { Text("Número interior (opcional)") })
            OutlinedTextField(value = suburb, onValueChange = { suburb = it }, label = { Text("Colonia") })
            OutlinedTextField(value = city, onValueChange = { city = it }, label = { Text("Ciudad") })
            OutlinedTextField(value = postalCode, onValueChange = { postalCode = it }, label = { Text("Código Postal") })

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val fullAddress = buildString {
                        append("$street #$extNumber")
                        if (intNumber.isNotBlank()) append(", Int. $intNumber")
                        if (suburb.isNotBlank()) append(", $suburb")
                        append(", $city")
                        if (postalCode.isNotBlank()) append(", CP $postalCode")
                    }
                    onSave(fullAddress)
                },
                enabled = isValid,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Guardar")
            }
        }
    }
}