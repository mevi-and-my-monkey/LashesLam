package com.mevi.lasheslam.ui.home.products

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
import com.mevi.lasheslam.network.ProductItem
import com.mevi.lasheslam.ui.home.products.components.BestSellingRow
import com.mevi.lasheslam.ui.home.products.components.CategoriesView
import com.mevi.lasheslam.ui.home.products.components.ProductsList

@Composable
fun ProductsHPContent(
    products: List<ProductItem>,
    isLoading: Boolean,
    categories: List<CategoryModel>,
    selectedCategoryId: String?,
    onCategorySelected: (CategoryModel) -> Unit,
    bestSellingProducts: List<ProductItem>,
    trackEvent: (AnalyticsEvent) -> Unit,
    favorites: Set<String>,
    onToggleFavorite: (String) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {

        CategoriesView(
            categories = categories,
            selectedCategoryId = selectedCategoryId,
            onCategorySelected = onCategorySelected
        )

        if (bestSellingProducts.isNotEmpty()) {
            Text(
                text = stringResource(R.string.best_selling),
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp, start = 16.dp),
                style = MaterialTheme.typography.titleMedium
            )
            BestSellingRow(
                products = bestSellingProducts, trackEvent = trackEvent,
                favorites = favorites,
                onToggleFavorite = onToggleFavorite
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
                text = stringResource(R.string.products_subtitle),
                fontSize = 24.sp,
                style = MaterialTheme.typography.titleMedium,
            )

            Text(
                text = "${products.size} " + stringResource(R.string.products_text),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

        }

        ProductsList(
            trackEvent = trackEvent,
            products = products,
            isLoading = isLoading,
            favorites = favorites,
            onToggleFavorite = onToggleFavorite
        ) { products ->
            //onNavigateToServiceDetails(service.id)
        }
    }
}