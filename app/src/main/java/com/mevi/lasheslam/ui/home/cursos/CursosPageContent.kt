package com.mevi.lasheslam.ui.home.cursos

import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mevi.lasheslam.network.ServiceItem
import com.mevi.lasheslam.ui.home.components.BannerView

@Composable
fun CursosPageContent(
    onNavigateToServiceDetails: (String) -> Unit,
    services: List<ServiceItem>,
    isLoading: Boolean
) {
    Column(modifier = Modifier.fillMaxSize()) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            BannerView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                onNavigateToServiceDetails = onNavigateToServiceDetails
            )
        }

        CursesList(
            services = services,
            isLoading = isLoading
        ) { service ->
            onNavigateToServiceDetails(service.id)
        }
    }
}