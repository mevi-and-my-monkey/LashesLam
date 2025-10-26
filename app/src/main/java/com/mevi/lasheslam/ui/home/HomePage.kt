package com.mevi.lasheslam.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mevi.lasheslam.session.SessionManager
import com.mevi.lasheslam.ui.auth.LoginViewModel
import com.mevi.lasheslam.ui.components.ErrorDialog
import com.mevi.lasheslam.ui.components.SuccessDialog

@Composable
fun HomePage(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var showSuccess by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }

    val isAdmin by SessionManager.isUserAdmin.collectAsState()
    val isInvited by SessionManager.isUserInvited.collectAsState()
    val whatsapp by SessionManager.whatsApp.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Página de inicio ${if (isAdmin) "administrador" else "usuario"}")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { showSuccess = true }) {
            Text("Mostrar éxito")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { showError = true }) {
            Text("Mostrar error")
        }
    }

    if (showSuccess) {
        SuccessDialog(onDismiss = { showSuccess = false })
    }

    if (showError) {
        ErrorDialog(onDismiss = { showError = false })
    }
}