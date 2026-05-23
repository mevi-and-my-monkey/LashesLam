package com.mevi.lasheslam.ui.products.add

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mevi.lasheslam.R
import com.mevi.lasheslam.ui.components.ErrorDialog
import com.mevi.lasheslam.ui.components.SuccessDialog
import com.mevi.lasheslam.ui.courses.components.AddTitleView
import com.mevi.lasheslam.ui.products.ProductsUiState
import com.mevi.lasheslam.ui.products.add.components.AddProdCategoryFormView
import com.mevi.lasheslam.ui.products.add.components.AddProdCostFormView
import com.mevi.lasheslam.ui.products.add.components.AddProdImagesView
import com.mevi.lasheslam.ui.products.add.components.AddProdTitleFormView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductsContent(
    state: ProductsUiState,
    onDismiss: () -> Unit,
    onTitleChange: (String) -> Unit = {},
    onCharacteristicsChange: (String) -> Unit = {},
    onDescriptionChange: (String) -> Unit = {},
    onCostChange: (String) -> Unit = {},
    onActualCostChange: (String) -> Unit = {},
    onCategoryChange: (String) -> Unit = {},
    onBestSellingChange: (Boolean) -> Unit = {},
    onImagesSelected: (List<Uri>) -> Unit = {},
    removeImage: (Uri) -> Unit = {},
    removeRemoteImage: (String) -> Unit = {},
    showSuccess: Boolean = false,
    successMessage: String = "",
    showError: Boolean = false,
    errorMessage: String? = null,
    onDismissSuccess: () -> Unit,
    onDismissError: () -> Unit,
    saveProduct: () -> Unit = {}
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            AddTitleView(stringResource(R.string.product_add_title))

            AddProdImagesView(
                state = state,
                onAddImages = onImagesSelected,
                onRemoveImage = removeImage,
                removeRemoteImage = removeRemoteImage
            )

            AddProdTitleFormView(
                state,
                onTitleChange,
                onDescriptionChange,
                onCharacteristicsChange = onCharacteristicsChange
            )

            AddProdCostFormView(state, onCostChange, onActualCostChange)

            AddProdCategoryFormView(state, onCategoryChange, onBestSellingChange)

            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = saveProduct,
                enabled = !state.isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(if (state.isLoading) stringResource(R.string.saving) else stringResource(R.string.save))
            }

            if (showSuccess) {
                SuccessDialog(
                    title = stringResource(R.string.curso_agregado),
                    message = successMessage,
                    onDismiss = onDismissSuccess,
                    onCancel = {}
                )
            }
            if (showError) {
                ErrorDialog(
                    title = stringResource(R.string.error),
                    message = errorMessage ?: "",
                    onDismiss = onDismissError,
                    onCancel = {}
                )
            }
        }
    }
}