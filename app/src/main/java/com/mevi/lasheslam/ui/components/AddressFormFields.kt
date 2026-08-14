package com.mevi.lasheslam.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mevi.lasheslam.utils.InputValidator
import com.mevi.lasheslam.utils.ValidationResult

/**
 * Campos de dirección reutilizables (calle, número ext/int, colonia, ciudad, CP)
 * con la misma validación y formato de dirección usados en toda la app.
 *
 * Notifica a [onAddressChange] con la dirección construida y si el formulario es
 * válido. Se usa en el perfil, el registro y el checkout del carrito.
 */
@Composable
fun AddressFormFields(
    initialAddress: String? = null,
    onAddressChange: (address: String, isValid: Boolean) -> Unit
) {
    var street by remember { mutableStateOf("") }
    var extNumber by remember { mutableStateOf("") }
    var intNumber by remember { mutableStateOf("") }
    var suburb by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var postalCode by remember { mutableStateOf("") }

    var streetValidation by remember { mutableStateOf(ValidationResult(true)) }
    var extNumberValidation by remember { mutableStateOf(ValidationResult(true)) }
    var intNumberValidation by remember { mutableStateOf(ValidationResult(true)) }
    var suburbValidation by remember { mutableStateOf(ValidationResult(true)) }
    var cityValidation by remember { mutableStateOf(ValidationResult(true)) }
    var postalValidation by remember { mutableStateOf(ValidationResult(true)) }

    // Prellenar cuando hay dirección existente (formato:
    // "Calle #NumExt, Int. NumInt, Colonia, Ciudad, CP 12345")
    LaunchedEffect(initialAddress) {
        val address = initialAddress ?: return@LaunchedEffect
        val parts = address.split(",")
        if (parts.isNotEmpty()) {
            val firstPart = parts[0].trim()
            if (firstPart.contains("#")) {
                street = firstPart.substringBefore("#").trim()
                extNumber = firstPart.substringAfter("#").trim()
            } else {
                street = firstPart
            }
        }
        parts.forEach { segment ->
            when {
                segment.contains("Int.", ignoreCase = true) ->
                    intNumber = segment.substringAfter("Int.").trim()

                segment.contains("CP", ignoreCase = true) ->
                    postalCode = segment.substringAfter("CP").trim()

                suburb.isEmpty() -> suburb = segment.trim()
                city.isEmpty() -> city = segment.trim()
            }
        }
    }

    fun notify() {
        val isValid = streetValidation.isValid &&
                extNumberValidation.isValid &&
                intNumberValidation.isValid &&
                suburbValidation.isValid &&
                cityValidation.isValid &&
                postalValidation.isValid &&
                street.isNotBlank() &&
                extNumber.isNotBlank() &&
                suburb.isNotBlank() &&
                city.isNotBlank() &&
                postalCode.isNotBlank()

        val fullAddress = buildString {
            append("$street #$extNumber")
            if (intNumber.isNotBlank()) append(", Int. $intNumber")
            append(", $suburb, $city, CP $postalCode")
        }
        onAddressChange(fullAddress, isValid)
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = street,
            onValueChange = {
                street = it.replaceFirstChar { c -> c.uppercase() }
                streetValidation = InputValidator.validateStreet(street)
                notify()
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

        OutlinedTextField(
            value = extNumber,
            onValueChange = {
                extNumber = it
                extNumberValidation = InputValidator.validateExtNumber(it)
                notify()
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

        OutlinedTextField(
            value = intNumber,
            onValueChange = {
                intNumber = it
                intNumberValidation = InputValidator.validateIntNumber(it)
                notify()
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

        OutlinedTextField(
            value = suburb,
            onValueChange = {
                suburb = it.replaceFirstChar { c -> c.uppercase() }
                suburbValidation = InputValidator.validateSuburb(suburb)
                notify()
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

        OutlinedTextField(
            value = city,
            onValueChange = {
                city = it.replaceFirstChar { c -> c.uppercase() }
                cityValidation = InputValidator.validateCity(city)
                notify()
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

        OutlinedTextField(
            value = postalCode,
            onValueChange = {
                postalCode = it.filter(Char::isDigit)
                postalValidation = InputValidator.validatePostalCode(postalCode)
                notify()
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
    }
}
