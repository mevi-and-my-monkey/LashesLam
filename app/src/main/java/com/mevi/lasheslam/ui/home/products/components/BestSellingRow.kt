package com.mevi.lasheslam.ui.home.products.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.mevi.lasheslam.domain.analytics.AnalyticsEvent
import com.mevi.lasheslam.network.ProductItem

@Composable
fun BestSellingRow(
    products: List<ProductItem>, trackEvent: (AnalyticsEvent) -> Unit,
    favorites: Set<String>,
    onToggleFavorite: (String) -> Unit,
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(products) { product ->
            BestSellingItem(product = product, trackEvent = trackEvent,
                isFavorite = favorites.contains(product.id),
                onToggleFavorite = { onToggleFavorite(product.id) },)
        }
    }
}