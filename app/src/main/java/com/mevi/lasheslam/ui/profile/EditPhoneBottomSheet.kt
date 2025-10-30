package com.mevi.lasheslam.ui.profile

import android.annotation.SuppressLint
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mevi.lasheslam.core.Strings
import com.mevi.lasheslam.ui.theme.LashesLamTheme
import com.mevi.lasheslam.utils.InputValidator
import com.mevi.lasheslam.utils.ValidationResult

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun EditPhoneBottomSheet(
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    val scrollState = rememberScrollState()
    var phone by remember { mutableStateOf("") }
    val phoneValidation = remember { mutableStateOf(ValidationResult(true)) }

    val isValid by derivedStateOf {
        phoneValidation.value.isValid &&
                phone.isNotBlank()
    }
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        tonalElevation = 8.dp,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .imeNestedScroll()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Editar número telefónico", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = phone,
                onValueChange = {
                    phone = it.filter(Char::isDigit)
                    phoneValidation.value = InputValidator.validatePhone(phone)
                },
                label = { Text(Strings.phoneNumber) },
                leadingIcon = {
                    Icon(
                        Icons.Default.Call,
                        contentDescription = Strings.phoneNumber
                    )
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                isError = !phoneValidation.value.isValid,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )
            if (!phoneValidation.value.isValid) {
                Text(
                    text = phoneValidation.value.errorMessage ?: "",
                    color = Color(0xFFFF6F6F),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onSave(phone) },
                enabled = isValid,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar")
            }
        }
    }
}

@Preview
@Composable
fun BottomSheetEditPhonePreview() {
    LashesLamTheme {
        EditPhoneBottomSheet({}, { })
    }
}