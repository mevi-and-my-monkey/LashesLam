package com.mevi.lasheslam.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddBusiness
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mevi.lasheslam.session.SessionManager
import com.mevi.lasheslam.ui.components.BottomSheetOption
import com.mevi.lasheslam.ui.components.GenericOptionsBottomSheet
import com.mevi.lasheslam.ui.home.components.BannerView
import com.mevi.lasheslam.ui.home.components.HeaderView

@Composable
fun HomePage(
    navController: NavController
) {
    val isAdmin by SessionManager.isUserAdmin.collectAsState()
    var showOptionsBottomSheet by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            HeaderView(navController)
            Spacer(modifier = Modifier.height(10.dp))
            BannerView(modifier = Modifier.height(400.dp))
        }

        if (isAdmin) {
            FloatingActionButton(
                onClick = {
                    showOptionsBottomSheet = true
                },
                containerColor = Color(0xFFFF80AB),
                contentColor = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(24.dp),
                elevation = FloatingActionButtonDefaults.elevation(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar",
                    tint = Color.White
                )
            }
        }

        if (showOptionsBottomSheet) {
            GenericOptionsBottomSheet(
                title = "Administrar productos y servicios",
                onDismiss = { showOptionsBottomSheet = false },
                options = listOf(
                    BottomSheetOption(
                        label = "Subir nuevo producto",
                        icon = Icons.Default.AddBusiness
                    ) {

                    },
                    BottomSheetOption(
                        label = "Subir nuevo evento",
                        icon = Icons.Default.PostAdd
                    ) {

                    }
                )
            )
        }
    }
}