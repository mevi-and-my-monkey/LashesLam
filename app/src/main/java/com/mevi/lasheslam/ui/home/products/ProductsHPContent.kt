package com.mevi.lasheslam.ui.home.products

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
import com.mevi.lasheslam.ui.home.cursos.components.ShimmerBox
import com.mevi.lasheslam.ui.home.products.components.AnimatedMarketplaceProductItem
import com.mevi.lasheslam.ui.home.products.components.BestSellingRow
import com.mevi.lasheslam.ui.home.products.components.CategoriesView

@Composable
fun ProductsHPContent(
    products: List<ProductItem>,
    isLoading: Boolean,
    onNavigateToProductsDetail: (String) -> Unit,
    categories: List<CategoryModel>,
    selectedCategoryId: String?,
    onCategorySelected: (CategoryModel) -> Unit,
    bestSellingProducts: List<ProductItem>,
    trackEvent: (AnalyticsEvent) -> Unit,
    favorites: Set<String>,
    onToggleFavorite: (String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {

        item {
            CategoriesView(
                categories = categories,
                selectedCategoryId = selectedCategoryId,
                onCategorySelected = onCategorySelected
            )
        }

        if (bestSellingProducts.isNotEmpty()) {

            item {
                Text(
                    text = stringResource(R.string.best_selling),
                    modifier = Modifier.padding(
                        top = 8.dp,
                        bottom = 4.dp,
                        start = 16.dp
                    ),
                    style = MaterialTheme.typography.titleMedium
                )
            }

            item {
                BestSellingRow(
                    onNavigateToProductsDetail = onNavigateToProductsDetail,
                    products = bestSellingProducts,
                    trackEvent = trackEvent,
                    favorites = favorites,
                    onToggleFavorite = onToggleFavorite
                )
            }
        }

        item {
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
        }

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