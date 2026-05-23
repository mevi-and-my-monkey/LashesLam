package com.mevi.lasheslam.ui.products.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mevi.lasheslam.R
import com.mevi.lasheslam.network.FavoriteItem
import com.mevi.lasheslam.ui.courses.details.CourseContent
import com.mevi.lasheslam.ui.courses.details.components.DetailDescriptionView
import com.mevi.lasheslam.ui.courses.details.components.DetailsOptionsTop
import com.mevi.lasheslam.ui.favorites.FavoriteType
import com.mevi.lasheslam.ui.products.ProductsUiState
import com.mevi.lasheslam.ui.products.ProductsViewModel
import com.mevi.lasheslam.ui.products.details.components.DetailBestSellerView
import com.mevi.lasheslam.ui.products.details.components.DetailCharactView
import com.mevi.lasheslam.ui.products.details.components.DetailCostProductView
import com.mevi.lasheslam.ui.products.details.components.DetailsImagesCourseView
import com.mevi.lasheslam.ui.products.details.components.DetailsSocialMediaCourView

@Composable
fun ProductDetailContent(
    modifier: Modifier,
    uiState: ProductsUiState,
    productId: String,
    onDismiss: () -> Unit,
    onEditClick: (String) -> Unit,
    showConfirmDelete: () -> Unit,
    onOpenFacebook: (String) -> Unit,
    onOpenInstagram: (String) -> Unit,
    onOpenWhatsApp: (String) -> Unit,
    favoritesList: List<FavoriteItem>,
    viewModel: ProductsViewModel = hiltViewModel()
) {

    val isFavorite = remember(favoritesList, productId) {
        favoritesList.any { it.itemId == productId && it.type == FavoriteType.PRODUCT.name }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(top = 16.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surface)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 180.dp)
        ) {
            DetailsImagesCourseView(images = uiState.productDetail.images)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 16.dp,
                        bottom = 120.dp
                    )
            ) {

                DetailBestSellerView(modifier = modifier)

                Spacer(Modifier.height(8.dp))

                CourseContent(titulo = uiState.productDetail.title)

                Spacer(modifier = Modifier.height(8.dp))

                if (uiState.productDetail.characteristics.isNotEmpty()) {
                    DetailCharactView(uiState.productDetail.characteristics)
                }

                DetailCostProductView(
                    currentPrice = uiState.productDetail.price,
                    originalPrice = uiState.productDetail.actulPrice,
                    modifier = modifier
                )

                Spacer(Modifier.height(8.dp))

                DetailDescriptionView(
                    descripcion = uiState.productDetail.description,
                    title = stringResource(R.string.description)
                )

                Spacer(Modifier.height(24.dp))

                Text(
                    text = stringResource(R.string.request_info),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Spacer(Modifier.height(12.dp))

                DetailsSocialMediaCourView(
                    onOpenFacebook = onOpenFacebook,
                    onOpenInstagram = onOpenInstagram,
                    onOpenWhatsApp = onOpenWhatsApp,
                    facebook = uiState.facebook ?: "",
                    instagram = uiState.instagram ?: "",
                    whatsApp = uiState.whatsApp ?: "",
                    titulo = uiState.productDetail.title,
                    precio = uiState.productDetail.price.toString(),
                )

                Spacer(Modifier.height(12.dp))

            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DetailsOptionsTop(
                uiState.isAdmin,
                isFavorite,
                productId,
                onEditClick,
                showConfirmDelete,
                onDismiss,
                toggleFavorite = { id ->
                    viewModel.toggleFavorite(id, FavoriteType.PRODUCT)
                }
            )
        }
    }
}