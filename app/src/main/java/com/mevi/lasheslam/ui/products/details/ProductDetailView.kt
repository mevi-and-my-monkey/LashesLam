package com.mevi.lasheslam.ui.products.details

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.mevi.lasheslam.ui.products.ProductsViewModel

@Composable
fun ProductDetailView(
    productId: String,
    onDismiss: () -> Unit,
    modifier: Modifier,
    onEditClick: (String) -> Unit,
    onOpenFacebook: (String) -> Unit,
    onOpenInstagram: (String) -> Unit,
    onOpenWhatsApp: (String) -> Unit,
    viewModel: ProductsViewModel = hiltViewModel()
) {

}