package com.mevi.lasheslam.ui.profile

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mevi.lasheslam.ui.theme.LashesLamTheme
import com.mevi.lasheslam.utils.InputValidator
import com.mevi.lasheslam.utils.ValidationResult

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun EditAddressBottomSheet(
    onDismiss: () -> Unit,
    onSave: (String) -> Unit,
    currentAddress: String? = null
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    var street by remember { mutableStateOf("") }
    var extNumber by remember { mutableStateOf("") }
    var intNumber by remember { mutableStateOf("") }
    var suburb by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var postalCode by remember { mutableStateOf("") }

    // Estado de validaciones
    var streetValidation by remember { mutableStateOf(ValidationResult(true)) }
    var extNumberValidation by remember { mutableStateOf(ValidationResult(true)) }
    var intNumberValidation by remember { mutableStateOf(ValidationResult(true)) }
    var suburbValidation by remember { mutableStateOf(ValidationResult(true)) }
    var cityValidation by remember { mutableStateOf(ValidationResult(true)) }
    var postalValidation by remember { mutableStateOf(ValidationResult(true)) }

    // Si hay dirección existente, prellenamos campos
    LaunchedEffect(currentAddress) {
        currentAddress?.let { address ->
            // Aquí dividimos en partes si el formato es el que tú guardas:
            // "Calle #NumExt, Int. NumInt, Colonia, Ciudad, CP 12345"
            val parts = address.split(",")
            if (parts.isNotEmpty()) {
                val firstPart = parts[0].trim()
                if (firstPart.contains("#")) {
                    val streetPart = firstPart.substringBefore("#").trim()
                    val extPart = firstPart.substringAfter("#").trim()
                    street = streetPart
                    extNumber = extPart
                } else {
                    street = firstPart
                }
            }
            parts.forEach { segment ->
                when {
                    segment.contains("Int.", ignoreCase = true) -> intNumber =
                        segment.substringAfter("Int.").trim()
                    segment.contains("CP", ignoreCase = true) -> postalCode =
                        segment.substringAfter("CP").trim()
                    // Los demás, si no coinciden, los tratamos como colonia o ciudad
                    suburb.isEmpty() -> suburb = segment.trim()
                    city.isEmpty() -> city = segment.trim()
                }
            }
        }
    }

    val isFormValid by derivedStateOf {
        streetValidation.isValid &&
                extNumberValidation.isValid &&
                suburbValidation.isValid &&
                cityValidation.isValid &&
                postalValidation.isValid &&
                street.isNotBlank() &&
                extNumber.isNotBlank() &&
                suburb.isNotBlank() &&
                city.isNotBlank() &&
                postalCode.isNotBlank()
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .imeNestedScroll()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Editar dirección", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(16.dp))

            // --- CALLE ---
            OutlinedTextField(
                value = street,
                onValueChange = {
                    street = it.replaceFirstChar { c -> c.uppercase() }
                    streetValidation = InputValidator.validateStreet(street)
                },
                label = { Text("Calle") },
                isError = !streetValidation.isValid,
                singleLine = true,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            if (!streetValidation.isValid) {
                Text(streetValidation.errorMessage ?: "", color = Color.Red, fontSize = 12.sp)
            }

            // --- NUM EXT ---
            OutlinedTextField(
                value = extNumber,
                onValueChange = {
                    extNumber = it
                    extNumberValidation = InputValidator.validateExtNumber(it)
                },
                label = { Text("Número exterior") },
                isError = !extNumberValidation.isValid,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
            )
            if (!extNumberValidation.isValid) {
                Text(extNumberValidation.errorMessage ?: "", color = Color.Red, fontSize = 12.sp)
            }

            // --- NUM INT ---
            OutlinedTextField(
                value = intNumber,
                onValueChange = {
                    intNumber = it
                    intNumberValidation = InputValidator.validateIntNumber(it)
                },
                label = { Text("Número interior (opcional)") },
                isError = !intNumberValidation.isValid,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
            )
            if (!intNumberValidation.isValid) {
                Text(intNumberValidation.errorMessage ?: "", color = Color.Red, fontSize = 12.sp)
            }

            // --- COLONIA ---
            OutlinedTextField(
                value = suburb,
                onValueChange = {
                    suburb = it.replaceFirstChar { c -> c.uppercase() }
                    suburbValidation = InputValidator.validateSuburb(suburb)
                },
                label = { Text("Colonia") },
                isError = !suburbValidation.isValid,
                singleLine = true,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
            )
            if (!suburbValidation.isValid) {
                Text(suburbValidation.errorMessage ?: "", color = Color.Red, fontSize = 12.sp)
            }

            // --- CIUDAD ---
            OutlinedTextField(
                value = city,
                onValueChange = {
                    city = it.replaceFirstChar { c -> c.uppercase() }
                    cityValidation = InputValidator.validateCity(city)
                },
                label = { Text("Ciudad") },
                isError = !cityValidation.isValid,
                singleLine = true,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
            )
            if (!cityValidation.isValid) {
                Text(cityValidation.errorMessage ?: "", color = Color.Red, fontSize = 12.sp)
            }

            // --- CP ---
            OutlinedTextField(
                value = postalCode,
                onValueChange = {
                    postalCode = it.filter(Char::isDigit)
                    postalValidation = InputValidator.validatePostalCode(postalCode)
                },
                label = { Text("Código Postal") },
                isError = !postalValidation.isValid,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
            )
            if (!postalValidation.isValid) {
                Text(postalValidation.errorMessage ?: "", color = Color.Red, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (street.isNotEmpty() && extNumber.isNotEmpty() && suburb.isNotEmpty() && city.isNotEmpty() && postalCode.isNotEmpty()) {
                        val fullAddress = buildString {
                            append("$street #$extNumber")
                            if (intNumber.isNotBlank()) append(", Int. $intNumber")
                            append(", $suburb, $city, CP $postalCode")
                        }
                        onSave(fullAddress)
                    } else {
                        Toast.makeText(
                            context,
                            "Por favor, completa todos los campos obligatorios.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                enabled = isFormValid,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Guardar")
            }
        }
    }
}

@Preview
@Composable
fun BottomSheetPreview() {
    LashesLamTheme {
        EditAddressBottomSheet({}, { })
    }
}