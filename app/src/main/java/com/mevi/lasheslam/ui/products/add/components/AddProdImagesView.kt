package com.mevi.lasheslam.ui.products.add.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mevi.lasheslam.R
import com.mevi.lasheslam.ui.products.ProductsUiState

@Composable
fun AddProdImagesView(
    state: ProductsUiState,
    onAddImages: (List<Uri>) -> Unit,
    onRemoveImage: (Uri) -> Unit
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        onAddImages(uris.take(5))
    }
    Column {
        Button(
            onClick = { launcher.launch("image/*") }
        ) { Text(stringResource(R.string.select_image)) }

        LazyRow {
            items(state.form.images) { image ->
                Box {
                    AsyncImage(
                        model = image,
                        contentDescription = null,
                        modifier = Modifier.size(100.dp)
                    )
                    IconButton(
                        onClick = { onRemoveImage(image) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null
                        )
                    }
                }
            }
        }

        Text("${state.form.images.size}/${stringResource(R.string.five_images)}")

    }

    Spacer(Modifier.height(16.dp))

}