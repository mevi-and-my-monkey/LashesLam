package com.mevi.lasheslam.ui.courses

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
import com.mevi.lasheslam.network.LocationItem
import com.mevi.lasheslam.ui.components.ErrorDialog
import com.mevi.lasheslam.ui.components.SuccessDialog
import com.mevi.lasheslam.ui.courses.components.AddCoachFormView
import com.mevi.lasheslam.ui.courses.components.AddCostFormView
import com.mevi.lasheslam.ui.courses.components.AddDateFormView
import com.mevi.lasheslam.ui.courses.components.AddHoursFormView
import com.mevi.lasheslam.ui.courses.components.AddImageView
import com.mevi.lasheslam.ui.courses.components.AddLocationFormView
import com.mevi.lasheslam.ui.courses.components.AddTemariosFormView
import com.mevi.lasheslam.ui.courses.components.AddTitleFormView
import com.mevi.lasheslam.ui.courses.components.AddTitleView
import com.mevi.lasheslam.ui.home.cursos.CourseUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCoursesContent(
    state: CourseUiState,
    onDismiss: () -> Unit,
    showSuccess: Boolean = false,
    successMessage: String = "",
    showError: Boolean = false,
    errorMessage: String? = null,
    onShowTimePickerInicio: () -> Unit,
    onShowTimePickerFin: () -> Unit,
    onShowDatePicker: () -> Unit,
    onImageChange: (Uri?) -> Unit,
    onInstructorImageChange: (Uri?) -> Unit = {},
    onTitleChange: (String) -> Unit = {},
    onDescriptionChange: (String) -> Unit = {},
    onCostChange: (String) -> Unit = {},
    onApartaChange: (String) -> Unit = {},
    onInstructorChange: (String) -> Unit = {},
    onInstructorDescChange: (String) -> Unit = {},
    onTemarioChange: (Int, String) -> Unit = { _, _ -> },
    saveCourse: () -> Unit = {},
    locations: List<LocationItem>,
    expanded: Boolean,
    selectedLocation: LocationItem?,
    onExpandedChange: (Boolean) -> Unit,
    onLocationSelected: (LocationItem) -> Unit,
    onDismissSuccess: () -> Unit,
    onDismissError: () -> Unit,
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
            AddTitleView(stringResource(R.string.course_add_title))

            AddImageView(state, onImageChange)

            AddTitleFormView(state, onTitleChange, onDescriptionChange)

            AddHoursFormView(state, onShowTimePickerInicio, onShowTimePickerFin)

            AddDateFormView(state, onShowDatePicker)

            AddCostFormView(state, onCostChange, onApartaChange)

            AddCoachFormView(
                state,
                onInstructorChange,
                onInstructorDescChange,
                onInstructorImageChange
            )

            AddLocationFormView(
                locations,
                expanded,
                selectedLocation,
                onExpandedChange,
                onLocationSelected
            )

            AddTemariosFormView(state, onTemarioChange)

            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = saveCourse,
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