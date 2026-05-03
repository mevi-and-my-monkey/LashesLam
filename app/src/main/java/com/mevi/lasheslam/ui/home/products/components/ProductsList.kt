package com.mevi.lasheslam.ui.home.products.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mevi.lasheslam.network.ProductItem
import com.mevi.lasheslam.ui.home.cursos.components.ShimmerBox

@Composable
fun ProductsList(
    products: List<ProductItem>,
    isLoading: Boolean,
    onClick: (ProductItem) -> Unit
) {
    Column {
        if (isLoading) {
            repeat(6) {
                ShimmerBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(8.dp)
            ) {
                itemsIndexed(products) { index, product ->
                    AnimatedMarketplaceProductItem(
                        products = product,
                        index = index,
                        onClick = { onClick(product) }
                    )
                }
            }
        }
    }
}