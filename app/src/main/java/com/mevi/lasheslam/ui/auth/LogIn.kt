package com.mevi.lasheslam.ui.auth

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.mevi.lasheslam.R
import com.mevi.lasheslam.domain.analytics.AnalyticsEvent
import com.mevi.lasheslam.ui.common.toUserMessage

@Composable
fun LogIn(onNavigateToHome: () -> Unit, loginViewModel: LoginViewModel = hiltViewModel()) {

    LaunchedEffect(Unit) {
        loginViewModel.trackScreen("login_screen")
    }
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

    LoginScreen(
        state = state,
        showLoginSheet = showLoginSheet,
        showRegisterSheet = showRegisterSheet,
        showSuccess = showSuccess,
        errorMessage = errorMessage,
        onLoginClick = {
            loginViewModel.trackEvent(AnalyticsEvent.LoginClick)
            showLoginSheet = true
        },
        onRegisterClick = {
            loginViewModel.trackEvent(AnalyticsEvent.RegisterClick)
            showRegisterSheet = true
        },
        onRegister = { user ->
            loginViewModel.register(user)
        },
        onEmailChange = {
            loginViewModel.onLoginChanged(it.trim(), state.password)
        },
        onPasswordChange = {
            loginViewModel.onLoginChanged(state.email, it.trim())
        },
        onLogin = { loginViewModel.login() },
        onGoogleClick = {
            loginViewModel.trackEvent(AnalyticsEvent.GoogleLoginClick)
            val opciones =
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(context.getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()
            val cliente = GoogleSignIn.getClient(context, opciones)
            cliente.signOut().addOnCompleteListener {
                launcher.launch(cliente.signInIntent)
            }
        },
        onDismissError = { errorMessage = null },
        onDismissSuccess = {
            showSuccess = false
            onNavigateToHome()
        },
        onCloseLogin = { showLoginSheet = false },
        onCloseRegister = { showRegisterSheet = false }
    )
}

