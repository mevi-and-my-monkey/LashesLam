package com.mevi.lasheslam.ui.home.services

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mevi.lasheslam.R
import com.mevi.lasheslam.domain.analytics.AnalyticsEvent
import com.mevi.lasheslam.network.CategoryModel
import com.mevi.lasheslam.network.ServiceItem
import com.mevi.lasheslam.ui.home.products.components.CategoriesView
import com.mevi.lasheslam.ui.home.services.components.ServicesList
import com.mevi.lasheslam.utils.Utilities

@Composable
fun ServicesHPContent(
    onNavigateToServiceEdit: (String) -> Unit,
    isLoading: Boolean,
    isAdmin: Boolean,
    whatsApp: String,
    categories: List<CategoryModel>,
    selectedCategoryId: String?,
    onCategorySelected: (CategoryModel) -> Unit,
    trackEvent: (AnalyticsEvent) -> Unit,
    services: List<ServiceItem>
) {
    Column(modifier = Modifier.fillMaxSize()) {

        CategoriesView(
            categories = categories,
            selectedCategoryId = selectedCategoryId,
            onCategorySelected = onCategorySelected
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.service_subtitle),
                fontSize = 24.sp,
                style = MaterialTheme.typography.titleMedium,
            )

            Text(
                text = stringResource(R.string.available_services),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

        }

        ServicesList(
            trackEvent = trackEvent,
            services = services,
            isLoading = isLoading,
            onClick = { service ->
                if (isAdmin) {
                    onNavigateToServiceEdit(service.id)

                }
            },
            onClickReservation = { service ->
                Utilities.createServiceMessageWhatsApp(
                    titulo = service.title,
                    precio = service.price.toString(),
                    whatsapp = whatsApp
                )
            }
        )
    }
}