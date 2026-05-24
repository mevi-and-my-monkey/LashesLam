package com.mevi.lasheslam.ui.products.search.lists

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mevi.lasheslam.domain.analytics.AnalyticsEvent
import com.mevi.lasheslam.network.ProductItem
import com.mevi.lasheslam.ui.home.cursos.components.ShimmerBox
import com.mevi.lasheslam.ui.home.products.components.AnimatedMarketplaceProductItem

@Composable
fun ProductsListSearch(
    products: List<ProductItem>,
    isLoading: Boolean,
    onNavigateToProductsDetail: (String) -> Unit,
    trackEvent: (AnalyticsEvent) -> Unit,
    favorites: Set<String>,
    onToggleFavorite: (String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
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

            itemsIndexed(
                items = products.chunked(2),
                key = { _, row ->
                    row.joinToString { it.id }
                }
            ) { rowIndex, rowProducts ->

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    rowProducts.forEachIndexed { columnIndex, product ->

                        val realIndex = (rowIndex * 2) + columnIndex

                        Box(
                            modifier = Modifier.weight(1f)
                        ) {

                            AnimatedMarketplaceProductItem(
                                trackEvent = trackEvent,
                                products = product,
                                index = realIndex,
                                favorites = favorites,
                                onToggleFavorite = onToggleFavorite,
                                onClick = {
                                    onNavigateToProductsDetail(product.id)
                                }
                            )
                        }
                    }

                    if (rowProducts.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}