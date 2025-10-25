package com.mevi.lasheslam.ui.auth

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mevi.lasheslam.core.Strings
import com.mevi.lasheslam.ui.components.GenericButton
import com.mevi.lasheslam.ui.theme.LashesLamTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginBottomSheet(
    onClose: () -> Unit,
    onLogin: (String, String) -> Unit,
    loginViewModel: LoginViewModel? = null
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onClose,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier.Companion
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.Companion.CenterHorizontally
        ) {
            // 🔹 Título e ícono de cerrar
            Row(
                modifier = Modifier.Companion.fillMaxWidth(),
                verticalAlignment = Alignment.Companion.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = Strings.login,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Companion.Bold),
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }

            Spacer(modifier = Modifier.Companion.height(16.dp))

            // 🔹 Campo de correo
            OutlinedTextField(
                value = loginViewModel?.email ?: "",
                onValueChange = {
                    loginViewModel?.onLoginChanged(email = it, password = loginViewModel.password)
                },
                label = { Text(Strings.email) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = Strings.emailHint
                    )
                },
                singleLine = true,
                modifier = Modifier.Companion.fillMaxWidth(),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.Companion.height(12.dp))

            // 🔹 Campo de contraseña
            var passwordVisible by remember { mutableStateOf(false) }
            OutlinedTextField(
                value = loginViewModel?.password ?: "",
                onValueChange = {
                    loginViewModel?.onLoginChanged(email = loginViewModel.email, password = it)
                },
                label = { Text(Strings.password) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = Strings.password
                    )
                },
                trailingIcon = {
                    val icon =
                        if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = icon, contentDescription = Strings.showHidePassword)
                    }
                },
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.Companion.None else PasswordVisualTransformation(),
                modifier = Modifier.Companion.fillMaxWidth(),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.Companion.height(24.dp))

            // 🔹 Botón de acceder
            GenericButton(
                Strings.access,
                onClick = {
                    onLogin(
                        loginViewModel?.email ?: "",
                        loginViewModel?.password ?: ""
                    )
                },
                backgroundColor = MaterialTheme.colorScheme.primary,
                textColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .height(52.dp),
                enabled = loginViewModel?.isLoginEnable ?: false
            )

            Spacer(modifier = Modifier.Companion.height(32.dp))

            // 🔹 Línea divisoria y pie
            Divider(
                modifier = Modifier.Companion
                    .fillMaxWidth(0.5f)
                    .height(1.dp),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
            )

            Spacer(modifier = Modifier.Companion.height(12.dp))

            Text(
                text = Strings.appNameByCreator,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Companion.Medium
                )
            )

            Spacer(modifier = Modifier.Companion.height(16.dp))
        }
    }
}

@Composable
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_DESK
)
fun Preview() {
    LashesLamTheme {
        LoginBottomSheet({}, { _, _ -> })
    }
}