package com.mevi.lasheslam.ui.auth

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.mevi.lasheslam.R
import com.mevi.lasheslam.core.Strings
import com.mevi.lasheslam.ui.common.toUserMessage
import com.mevi.lasheslam.ui.components.AnimatedLogo
import com.mevi.lasheslam.ui.components.ErrorDialog
import com.mevi.lasheslam.ui.components.GenericButton
import com.mevi.lasheslam.ui.components.GenericLoading
import com.mevi.lasheslam.ui.components.GenericOutlinedButton
import com.mevi.lasheslam.ui.components.SuccessDialog
import com.mevi.lasheslam.ui.components.WavyBackground

@Composable
fun LogIn(onNavigateToHome: () -> Unit, loginViewModel: LoginViewModel = hiltViewModel()) {
    var showLoginSheet by remember { mutableStateOf(false) }
    var showRegisterSheet by remember { mutableStateOf(false) }
    val context = LocalContext.current

    var showSuccess by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val state by loginViewModel.uiState.collectAsState()

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val account = task.getResult(ApiException::class.java)
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)

                loginViewModel.signInWithGoogle(
                    credential = credential,
                    email = account.email
                )

            } catch (e: Exception) {
                loginViewModel.onError(e)
            }
        }

    WavyBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp)
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
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
                    textColor = MaterialTheme.colorScheme.outline
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

                GenericOutlinedButton(
                    text = stringResource(R.string.login_with_google),
                    onClick = {
                        val opciones =
                            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestIdToken(context.getString(R.string.default_web_client_id))
                                .requestEmail()
                                .build()
                        val googleSignInCliente = GoogleSignIn.getClient(context, opciones)
                        googleSignInCliente.signOut().addOnCompleteListener {
                            launcher.launch(googleSignInCliente.signInIntent)
                        }
                    },
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
            onEmailChange = {
                loginViewModel.onLoginChanged(it.trim(), state.password)
            },
            onPasswordChange = {
                loginViewModel.onLoginChanged(state.email, it.trim())
            },
            onLogin = {
                loginViewModel.login()
            },
            onClose = { showLoginSheet = false })
    }

    if (showRegisterSheet) {
        RegisterBottomSheet(
            onCancel = { showRegisterSheet = false },
            onRegister = { user ->
                loginViewModel.register(user)
            }
        )
    }

    /***
     * OPERACION EXITOSA
     */
    if (showSuccess) {
        SuccessDialog(
            message = stringResource(R.string.success_generic),
            onDismiss = {
                showSuccess = false
                onNavigateToHome()
            },
            onCancel = {}
        )
    }

    errorMessage?.let { message ->
        ErrorDialog(
            message = message,
            onDismiss = {
                errorMessage = null
            },
            onCancel = {}
        )
    }

    LaunchedEffect(Unit) {
        loginViewModel.events.collect { event ->
            when (event) {

                is LoginUiEvent.NavigateToHome -> {
                    showLoginSheet = false
                    onNavigateToHome()
                }

                is LoginUiEvent.RegisterSuccess -> {
                    showRegisterSheet = false
                    showSuccess = true
                }


                is LoginUiEvent.ShowError -> errorMessage = event.error.toUserMessage()

            }
        }
    }
}

