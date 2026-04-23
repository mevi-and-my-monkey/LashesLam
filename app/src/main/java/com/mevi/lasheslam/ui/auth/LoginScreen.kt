package com.mevi.lasheslam.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mevi.lasheslam.R
import com.mevi.lasheslam.network.UserModel
import com.mevi.lasheslam.ui.components.AnimatedLogo
import com.mevi.lasheslam.ui.components.ErrorDialog
import com.mevi.lasheslam.ui.components.GenericButton
import com.mevi.lasheslam.ui.components.GenericLoading
import com.mevi.lasheslam.ui.components.GenericOutlinedButton
import com.mevi.lasheslam.ui.components.SuccessDialog
import com.mevi.lasheslam.ui.components.WavyBackground

@Composable
fun LoginScreen(
    state: LoginUiState,
    showLoginSheet: Boolean,
    showRegisterSheet: Boolean,
    showSuccess: Boolean,
    errorMessage: String?,

    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onRegister: (UserModel) -> Unit,
    onLogin: () -> Unit,
    onGoogleClick: () -> Unit,

    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,

    onDismissError: () -> Unit,
    onDismissSuccess: () -> Unit,
    onCloseLogin: () -> Unit,
    onCloseRegister: () -> Unit
) {

    WavyBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp)
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 100.dp)
            ) {
                AnimatedLogo()
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.welcome),
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier.padding(bottom = 24.dp),
                    fontStyle = FontStyle.Italic
                )

                GenericButton(
                    text = stringResource(R.string.login),
                    onClick = onLoginClick,
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    textColor = MaterialTheme.colorScheme.onPrimary
                )

                Spacer(modifier = Modifier.height(12.dp))

                GenericOutlinedButton(
                    text = stringResource(R.string.register),
                    onClick = onRegisterClick,
                    textColor = MaterialTheme.colorScheme.outline
                )
            }

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
                        text = stringResource(R.string.continue_with),
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

                GenericOutlinedButton(
                    text = stringResource(R.string.login_with_google),
                    onClick = onGoogleClick,
                    textColor = Color.Gray,
                    borderColor = Color.Gray,
                    icon = painterResource(id = R.drawable.ic_google_one)
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

        }

        GenericLoading(
            isLoading = state.isLoading,
            message = stringResource(R.string.loading_generic)
        )
    }

    if (showLoginSheet) {
        LoginBottomSheet(
            email = state.email,
            password = state.password,
            isEnabled = state.isLoginEnabled,
            onEmailChange = onEmailChange,
            onPasswordChange = onPasswordChange,
            onLogin = onLogin,
            onClose = onCloseLogin
        )
    }

    if (showRegisterSheet) {
        RegisterBottomSheet(
            onCancel = onCloseRegister,
            onRegister = onRegister
        )
    }

    if (showSuccess) {
        SuccessDialog(
            message = stringResource(R.string.success_generic),
            onDismiss = onDismissSuccess,
            onCancel = onDismissSuccess
        )
    }

    errorMessage?.let {
        ErrorDialog(
            message = it,
            onDismiss = onDismissError,
            onCancel = onDismissError
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(
        state = LoginUiState(
            email = "",
            password = "",
            isLoginEnabled = false,
            isLoading = false
        ),
        showLoginSheet = false,
        showRegisterSheet = false,
        showSuccess = false,
        errorMessage = null,

        onLoginClick = {},
        onRegisterClick = {},
        onRegister = {},
        onLogin = {},
        onGoogleClick = {},

        onEmailChange = {},
        onPasswordChange = {},

        onDismissError = {},
        onDismissSuccess = {},
        onCloseLogin = {},
        onCloseRegister = {}
    )
}