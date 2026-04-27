package com.mevi.lasheslam.ui.home.cursos

import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mevi.lasheslam.R
import com.mevi.lasheslam.network.CoursesItem
import com.mevi.lasheslam.ui.home.components.BannerView

@Composable
fun CursosPageContent(
    onNavigateToSearch: () -> Unit,
    onNavigateToServiceDetails: (String) -> Unit,
    services: List<CoursesItem>,
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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.courses_available),
                fontSize = 24.sp,
                style = MaterialTheme.typography.titleMedium,
            )
            TextButton(onClick = onNavigateToSearch) {
                Text(
                    text = stringResource(R.string.see_all),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        CursesList(
            services = services,
            isLoading = isLoading
        ) { service ->
            onNavigateToServiceDetails(service.id)
        }
    }
}