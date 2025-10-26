package com.mevi.lasheslam.ui.auth

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mevi.lasheslam.core.Strings
import com.mevi.lasheslam.ui.components.GenericButton
import com.mevi.lasheslam.ui.components.GenericOutlinedButton
import com.mevi.lasheslam.ui.theme.LashesLamTheme
import com.mevi.lasheslam.utils.InputValidator
import com.mevi.lasheslam.utils.ValidationResult

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun RegisterBottomSheet(
    onCancel: () -> Unit,
    onRegister: (String, String, String, String, String) -> Unit
) {
    val scrollState = rememberScrollState()
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val nameValidation = remember { mutableStateOf(ValidationResult(true)) }
    val emailValidation = remember { mutableStateOf(ValidationResult(true)) }
    val passwordValidation = remember { mutableStateOf(ValidationResult(true)) }
    val confirmValidation = remember { mutableStateOf(ValidationResult(true)) }
    val phoneValidation = remember { mutableStateOf(ValidationResult(true)) }

    val isFormValid by derivedStateOf {
        nameValidation.value.isValid &&
                emailValidation.value.isValid &&
                passwordValidation.value.isValid &&
                confirmValidation.value.isValid &&
                phoneValidation.value.isValid &&
                fullName.isNotBlank() &&
                email.isNotBlank() &&
                password.isNotBlank() &&
                confirmPassword.isNotBlank() &&
                phone.isNotBlank()
    }

    ModalBottomSheet(
        onDismissRequest = onCancel,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .imeNestedScroll() // 👈 permite que el teclado “empuje” el contenido
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🔹 Título e ícono de cerrar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = Strings.createAccount,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 🔹 Campos de formulario
            OutlinedTextField(
                value = fullName,
                onValueChange = {
                    fullName = it
                    nameValidation.value = InputValidator.validateName(it)
                },
                label = { Text(Strings.fullName) },
                leadingIcon = {
                    Icon(
                        Icons.Default.AccountCircle,
                        contentDescription = Strings.fullName
                    )
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                isError = !nameValidation.value.isValid
            )
            if (!nameValidation.value.isValid) {
                Text(
                    text = nameValidation.value.errorMessage ?: "",
                    color = Color(0xFFFF6F6F),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailValidation.value = InputValidator.validateEmail(it)
                },
                label = { Text(Strings.email) },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = Strings.emailHint) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                isError = !emailValidation.value.isValid
            )
            if (!emailValidation.value.isValid) {
                Text(
                    text = emailValidation.value.errorMessage ?: "",
                    color = Color(0xFFFF6F6F),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordValidation.value = InputValidator.validatePassword(it)
                },
                label = { Text(Strings.password) },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = Strings.password) },
                trailingIcon = {
                    val icon =
                        if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = icon, contentDescription = Strings.showHidePassword)
                    }
                },
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                isError = !passwordValidation.value.isValid
            )
            if (!passwordValidation.value.isValid) {
                Text(
                    text = passwordValidation.value.errorMessage ?: "",
                    color = Color(0xFFFF6F6F),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    confirmValidation.value = InputValidator.validateConfirmPassword(password, it)
                },
                label = { Text(Strings.confirmPassword) },
                leadingIcon = {
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = Strings.confirmPassword
                    )
                },
                trailingIcon = {
                    val icon =
                        if (confirmPasswordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(imageVector = icon, contentDescription = Strings.showHidePassword)
                    }
                },
                singleLine = true,
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                isError = !confirmValidation.value.isValid
            )
            if (!confirmValidation.value.isValid) {
                Text(
                    text = confirmValidation.value.errorMessage ?: "",
                    color = Color(0xFFFF6F6F),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

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

            Spacer(modifier = Modifier.height(24.dp))

            // 🔹 Botones registrar y cancelar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                GenericButton(
                    Strings.registerButton,
                    onClick = {
                        onRegister(
                            fullName,
                            email,
                            password,
                            confirmPassword,
                            phone
                        )
                    },
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    textColor = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.weight(1f),
                    enabled = isFormValid
                )

                GenericOutlinedButton(
                    text = Strings.cancelButton,
                    onClick = { onCancel() },
                    textColor = Color.Gray,
                    borderColor = Color.Gray,
                    modifier = Modifier.weight(1f),
                )

            }

            Spacer(modifier = Modifier.height(32.dp))

            // 🔹 Línea divisoria y pie
            Divider(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(1.dp),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = Strings.appNameByCreator,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium
                )
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
@Preview(
)
fun PreviewSignUp() {
    LashesLamTheme {
        RegisterBottomSheet({}, { _, _, _, _, _ -> })
    }
}
