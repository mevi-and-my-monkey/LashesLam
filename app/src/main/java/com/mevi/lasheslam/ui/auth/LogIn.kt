package com.mevi.lasheslam.ui.auth

import android.util.Log
import android.widget.Toast
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.mevi.lasheslam.R
import com.mevi.lasheslam.LashesLamApp
import com.mevi.lasheslam.core.Strings
import com.mevi.lasheslam.network.UserModel
import com.mevi.lasheslam.session.SessionManager
import com.mevi.lasheslam.ui.components.AnimatedLogo
import com.mevi.lasheslam.ui.components.GenericButton
import com.mevi.lasheslam.ui.components.GenericIconButton
import com.mevi.lasheslam.ui.components.GenericLoading
import com.mevi.lasheslam.ui.components.GenericOutlinedButton
import com.mevi.lasheslam.ui.components.WavyBackground
import com.mevi.lasheslam.utils.Utilities

@Composable
fun LogIn(navController: NavHostController, loginViewModel: LoginViewModel = hiltViewModel()) {
    var showLoginSheet by remember { mutableStateOf(false) }
    var showRegisterSheet by remember { mutableStateOf(false) }
    val isLoading = loginViewModel.isLoading

    val context = LocalContext.current

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
            loginViewModel.showLoading()
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val account = task.getResult(ApiException::class.java)
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)

                loginViewModel.signInWithGoogle(credential) { success, resultMessage ->
                    if (success) {
                        LashesLamApp.Companion.userAdmin =
                            Utilities.isAdmin(loginViewModel, account.email ?: "Sin dato")
                        LashesLamApp.Companion.userInvited = false
                        SessionManager.setAdmin(
                            Utilities.isAdmin(
                                loginViewModel,
                                account.email ?: "Sin dato"
                            )
                        )
                        SessionManager.setInvited(false)
                        loginViewModel.hideLoading()
                        navController.navigate("home") {
                            launchSingleTop = true
                        }
                    } else {
                        Toast.makeText(context, resultMessage ?: "Error", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                Log.d("Google_aut", "Google SignIn fallo")
            }

        }

    WavyBackground(
        backgroundColor = MaterialTheme.colorScheme.background,
        bigWaveColor = MaterialTheme.colorScheme.surfaceVariant,
        smallWaveColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier.Companion
                .fillMaxSize()
                .padding(horizontal = 32.dp)
                .navigationBarsPadding(), // deja espacio por la barra de gestos
            horizontalAlignment = Alignment.Companion.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween // reparte en partes
        ) {
            // --- Parte superior (logo + ola) ---
            Column(
                horizontalAlignment = Alignment.Companion.CenterHorizontally,
                modifier = Modifier.Companion.padding(top = 100.dp)
            ) {
                AnimatedLogo()
            }

            // --- Parte central (texto + botones) ---
            Column(
                horizontalAlignment = Alignment.Companion.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    Strings.welcome,
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier.Companion.padding(bottom = 24.dp),
                    fontStyle = FontStyle.Companion.Italic
                )

                GenericButton(
                    text = Strings.login,
                    onClick = { showLoginSheet = true },
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    textColor = MaterialTheme.colorScheme.onPrimary
                )

                Spacer(modifier = Modifier.Companion.height(12.dp))

                GenericOutlinedButton(
                    text = Strings.register,
                    onClick = { showRegisterSheet = true },
                )
            }

            // --- Parte inferior (continuar con...) ---
            Column(
                horizontalAlignment = Alignment.Companion.CenterHorizontally,
                modifier = Modifier.Companion.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.Companion.CenterVertically,
                    modifier = Modifier.Companion.fillMaxWidth()
                ) {
                    HorizontalDivider(
                        modifier = Modifier.Companion.weight(1f),
                        thickness = DividerDefaults.Thickness,
                        color = Color.Companion.LightGray
                    )

                    Text(
                        text = Strings.continueWith,
                        color = Color.Companion.Gray,
                        fontSize = 14.sp
                    )

                    HorizontalDivider(
                        modifier = Modifier.Companion.weight(1f),
                        thickness = DividerDefaults.Thickness,
                        color = Color.Companion.LightGray
                    )
                }

                Spacer(modifier = Modifier.Companion.height(24.dp))

                GenericIconButton(
                    icon = painterResource(id = R.drawable.ic_google_one),
                    contentDescription = "Acceder con Google",
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
                    backgroundColor = Color.Companion.White,
                    iconTint = Color.Companion.Unspecified
                )

                Spacer(modifier = Modifier.Companion.height(16.dp)) // margen final
            }
        }
        GenericLoading(
            isLoading = isLoading,
            message = "Procesando, por favor espera..."
        )
    }

    if (showLoginSheet) {
        LoginBottomSheet(
            onClose = { showLoginSheet = false },
            onLogin = { email, password ->
                loginViewModel.showLoading()
                loginViewModel.login(
                ) { success, resultMessage ->
                    if (success) {
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    } else {
                        Toast.makeText(context, resultMessage ?: "Error", Toast.LENGTH_LONG).show()
                    }
                }
            },
            loginViewModel
        )
    }

    if (showRegisterSheet) {
        RegisterBottomSheet(
            { showRegisterSheet = false },
            { name, email, password, _, phone ->
                loginViewModel.showLoading()
                val request = UserModel(name, email, password, phone)
                loginViewModel.register(request) { success, resultMessage ->
                    if (success) {
                        if (success) {
                            LashesLamApp.Companion.userAdmin =
                                Utilities.isAdmin(loginViewModel, email)
                            SessionManager.setAdmin(Utilities.isAdmin(loginViewModel, email))
                            SessionManager.setInvited(false)
                            navController.navigate("home") {
                                launchSingleTop = true
                            }
                        } else {
                            Toast.makeText(context, resultMessage ?: "Error", Toast.LENGTH_LONG)
                                .show()
                        }
                    } else {
                        loginViewModel.hideLoading()
                        Toast.makeText(context, "$resultMessage", Toast.LENGTH_LONG).show()
                        Log.i("ERROR_MESSAGE", "$resultMessage")
                    }
                }
                showRegisterSheet = false
            })
    }
}

