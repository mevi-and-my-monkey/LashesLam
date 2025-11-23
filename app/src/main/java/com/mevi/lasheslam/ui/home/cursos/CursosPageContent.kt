package com.mevi.lasheslam.ui.home.cursos

import android.os.Build
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mevi.lasheslam.navigation.Screen
import com.mevi.lasheslam.network.ServiceItem
import com.mevi.lasheslam.ui.home.components.BannerView

@Composable
fun CursosPageContent(
    navController: NavController,
    services: List<ServiceItem>,
    isLoading: Boolean
) {
    Column(modifier = Modifier.fillMaxSize()) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            BannerView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                navController = navController
            )
        }

        CursesList(
            services = services,
            isLoading = isLoading
        ) { service ->
            navController.navigate(
                Screen.ServiceDetails.createRoute(service.id)
            )
        }
    }
}