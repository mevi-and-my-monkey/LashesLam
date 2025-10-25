package com.mevi.lasheslam.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mevi.lasheslam.ui.components.AnimatedLogo
import com.mevi.lasheslam.ui.components.GenericButton
import com.mevi.lasheslam.ui.components.GenericIconButton
import com.mevi.lasheslam.ui.components.GenericOutlinedButton
import com.mevi.lasheslam.ui.auth.LoginBottomSheet
import com.mevi.lasheslam.R
import com.mevi.lasheslam.ui.auth.RegisterBottomSheet
import com.mevi.lasheslam.core.Strings
import com.mevi.lasheslam.ui.components.WavyBackground
import com.mevi.lasheslam.ui.theme.LashesLamTheme

@Composable
fun LogIn() {
    var showLoginSheet by remember { mutableStateOf(false) }
    var showRegisterSheet by remember { mutableStateOf(false) }

    WavyBackground(
        backgroundColor = MaterialTheme.colorScheme.background,
        bigWaveColor = MaterialTheme.colorScheme.surfaceVariant,
        smallWaveColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp)
                .navigationBarsPadding(), // deja espacio por la barra de gestos
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween // reparte en partes
        ) {
            // --- Parte superior (logo + ola) ---
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 100.dp)
            ) {
                AnimatedLogo()
            }

            // --- Parte central (texto + botones) ---
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    Strings.welcome,
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier.padding(bottom = 24.dp),
                    fontStyle = FontStyle.Italic
                )

                GenericButton(
                    text = Strings.login,
                    onClick = { showLoginSheet = true },
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    textColor = MaterialTheme.colorScheme.onPrimary
                )

                Spacer(modifier = Modifier.height(12.dp))

                GenericOutlinedButton(
                    text = Strings.register,
                    onClick = { showRegisterSheet = true },
                )
            }

            // --- Parte inferior (continuar con...) ---
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        thickness = DividerDefaults.Thickness,
                        color = Color.LightGray
                    )

                    Text(
                        text = Strings.continueWith,
                        color = Color.Gray,
                        fontSize = 14.sp
                    )

                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        thickness = DividerDefaults.Thickness,
                        color = Color.LightGray
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                GenericIconButton(
                    icon = painterResource(id = R.drawable.ic_google_one),
                    contentDescription = "Acceder con Google",
                    onClick = { /* Acción Registro */ },
                    backgroundColor = Color.White,
                    iconTint = Color.Unspecified
                )

                Spacer(modifier = Modifier.height(16.dp)) // margen final
            }
        }
    }

    if (showLoginSheet) {
        LoginBottomSheet(
            onClose = { showLoginSheet = false },
            onLogin = { email, password ->

            }
        )
    }

    if (showRegisterSheet) {
        RegisterBottomSheet({ showRegisterSheet = false },
            { _, _, _, _, _ -> })
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Login")
@Composable
fun LoginPreview() {
    LashesLamTheme {
        LogIn()
    }
}