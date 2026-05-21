package com.mevi.lasheslam.ui.home.products.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mevi.lasheslam.R
import com.mevi.lasheslam.domain.analytics.AnalyticsEvent
import com.mevi.lasheslam.network.ProductItem

@Composable
fun BestSellingItem(
    onNavigateToProductsDetail: (String) -> Unit,
    product: ProductItem,
    trackEvent: (AnalyticsEvent) -> Unit,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
) {
    Column(
        modifier = Modifier
            .width(100.dp)
            .clickable {
                trackEvent(AnalyticsEvent.ProductClick(product.title))
                onNavigateToProductsDetail(product.id)
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        ) {
            AsyncImage(
                model = product.images.firstOrNull(),
                contentDescription = product.title,
                modifier = Modifier.fillMaxSize()
            )

            IconButton(
                onClick = onToggleFavorite,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(4.dp)
                    .background(
                        Color.Black.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(50)
                    )
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = stringResource(R.string.favorites),
                    tint = if (isFavorite) Color.Red else Color.White
                )
            }
        }

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