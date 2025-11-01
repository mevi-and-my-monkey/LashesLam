package com.mevi.lasheslam.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mevi.lasheslam.session.SessionManager
import com.mevi.lasheslam.ui.home.components.HeaderView

@Composable
fun HomePage(
    navController: NavController,
) {
    val isAdmin by SessionManager.isUserAdmin.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            HeaderView(navController)
        }

        if (isAdmin) {
            FloatingActionButton(
                onClick = {
                    // Navegar a la pantalla de agregar producto, por ejemplo

                },
                containerColor = Color(0xFFFF80AB), // Rosa
                contentColor = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(24.dp), // separa del borde
                elevation = FloatingActionButtonDefaults.elevation(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar",
                    tint = Color.White
                )
            }
        }
    }
}