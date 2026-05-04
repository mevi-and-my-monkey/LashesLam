package com.mevi.lasheslam.ui.home.products.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mevi.lasheslam.domain.analytics.AnalyticsEvent
import com.mevi.lasheslam.network.ProductItem

@Composable
fun BestSellingItem(
    product: ProductItem, trackEvent: (AnalyticsEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .width(100.dp)
            .clickable {
                trackEvent(AnalyticsEvent.ProductClick(product.title))
            }
    ) {
        AsyncImage(
            model = product.images.firstOrNull(),
            contentDescription = product.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        )

        Text(
            text = product.title,
            maxLines = 1,
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = "$${product.actualPrice}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}