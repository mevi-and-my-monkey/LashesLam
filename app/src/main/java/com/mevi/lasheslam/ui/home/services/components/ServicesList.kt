package com.mevi.lasheslam.ui.home.services.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mevi.lasheslam.domain.analytics.AnalyticsEvent
import com.mevi.lasheslam.network.ServiceItem
import com.mevi.lasheslam.ui.home.cursos.components.ShimmerBox

@Composable
fun ServicesList(
    trackEvent: (AnalyticsEvent) -> Unit,
    services: List<ServiceItem>,
    isLoading: Boolean,
    onClick: (ServiceItem) -> Unit
) {
    LazyColumn {
        if (isLoading) {
            items(6) {
                ShimmerBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                )
            }
        } else {

            itemsIndexed(services) { index, service ->
                AnimatedMarketplaceServiceItem(
                    trackEvent = trackEvent,
                    services = service,
                    index = index,
                    onClick = { onClick(service) }
                )
            }
        }
    }
}