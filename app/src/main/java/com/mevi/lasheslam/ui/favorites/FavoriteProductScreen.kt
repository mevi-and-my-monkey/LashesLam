package com.mevi.lasheslam.ui.favorites

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mevi.lasheslam.network.ProductItem
import com.mevi.lasheslam.ui.components.views.EmptyViewScreen
import com.mevi.lasheslam.ui.favorites.products.FavoriteProductCard

@Composable
fun FavoriteProductScreen(
    onNavigateToProductsDetail: (String) -> Unit,
    favoriteProducts: List<ProductItem>,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            if (favoriteProducts.isEmpty()) {
                EmptyViewScreen()
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(favoriteProducts) { product ->
                        FavoriteProductCard(product = product) {
                            onNavigateToProductsDetail(product.id)
                        }
                    }
                }
            }
        }
    }
}
