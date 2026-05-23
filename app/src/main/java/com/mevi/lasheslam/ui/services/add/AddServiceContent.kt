package com.mevi.lasheslam.ui.services.add

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
import com.mevi.lasheslam.ui.services.ServiceUiState
import com.mevi.lasheslam.ui.services.add.components.AddIProdmageView
import com.mevi.lasheslam.ui.services.add.components.AddServCategoryFormView
import com.mevi.lasheslam.ui.services.add.components.AddServCostFormView
import com.mevi.lasheslam.ui.services.add.components.AddServTitleFormView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddServiceContent(
    state: ServiceUiState,
    onDismiss: () -> Unit,
    showSuccess: Boolean = false,
    successMessage: String = "",
    showError: Boolean = false,
    errorMessage: String? = null,
    onDismissSuccess: () -> Unit,
    onDismissError: () -> Unit,
    saveService: () -> Unit = {},
    onTitleChange: (String) -> Unit = {},
    onSubtitleChange: (String) -> Unit = {},
    onCostChange: (String) -> Unit = {},
    onDurationChange: (String) -> Unit = {},
    onCategoryChange: (String) -> Unit = {},
    onImageChange: (Uri?) -> Unit
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
            AddTitleView(stringResource(R.string.service_add_title))

            AddIProdmageView(state, onImageChange)

            AddServTitleFormView(state, onTitleChange, onSubtitleChange)

            AddServCostFormView(state, onCostChange, onDurationChange)

            AddServCategoryFormView(state, onCategoryChange)

            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = saveService,
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
                    title = stringResource(R.string.servicio_agregado),
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