package com.mevi.lasheslam.ui.courses

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.mevi.lasheslam.R
import com.mevi.lasheslam.network.LocationItem
import com.mevi.lasheslam.ui.components.ErrorDialog
import com.mevi.lasheslam.ui.components.SuccessDialog
import com.mevi.lasheslam.ui.home.cursos.CourseUiState
import com.mevi.lasheslam.utils.Constants
import java.text.NumberFormat
import java.util.Locale

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
    onImageChange: (Uri?) -> Unit ,
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
    val costoFormateado = remember(state.form.costo) {
        state.form.costo.toDoubleOrNull()?.let {
            NumberFormat.getCurrencyInstance(
                Locale(
                    Constants.Project.LANGUAGE,
                    Constants.Project.COUNTRY
                )
            ).format(it)
        } ?: ""
    }

    val apartadpFormateado = remember(state.form.apartado) {
        state.form.apartado.toDoubleOrNull()?.let {
            NumberFormat.getCurrencyInstance(
                Locale(
                    Constants.Project.LANGUAGE,
                    Constants.Project.COUNTRY
                )
            ).format(it)
        } ?: ""
    }

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> onImageChange(uri) }

    val pickImageLauncherInstr = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> onInstructorImageChange(uri) }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                stringResource(R.string.course_add_title),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable { pickImageLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (state.form.imageUri == null) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Icon(
                            imageVector = Icons.Outlined.CameraAlt,
                            contentDescription = stringResource(R.string.camera),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(32.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = stringResource(R.string.add_image_placeholder),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = stringResource(R.string.image_details),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }

                } else {
                    SubcomposeAsyncImage(
                        model = state.form.imageUri,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = state.form.titulo,
                onValueChange = { onTitleChange(it) },
                label = { Text(stringResource(R.string.title)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next
                ),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = state.form.descripcion,
                onValueChange = { onDescriptionChange(it) },
                label = { Text(stringResource(R.string.description)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 100.dp),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = state.form.horaInicio,
                    onValueChange = { },
                    readOnly = true,
                    label = { Text(stringResource(R.string.hora_inicio)) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    trailingIcon = {
                        IconButton(onClick = onShowTimePickerInicio) {
                            Icon(Icons.Default.AccessTime, contentDescription = null)
                        }
                    }
                )
                OutlinedTextField(
                    value = state.form.horaFin,
                    onValueChange = {  },
                    readOnly = true,
                    label = { Text(stringResource(R.string.hora_fin)) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    trailingIcon = {
                        IconButton(onClick = onShowTimePickerFin) {
                            Icon(Icons.Default.AccessTime, contentDescription = null)
                        }
                    }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = state.form.fecha,
                onValueChange = {  },
                readOnly = true,
                label = { Text(stringResource(R.string.fecha)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                trailingIcon = {
                    IconButton(onClick = onShowDatePicker) {
                        Icon(Icons.Default.CalendarMonth, contentDescription = null)
                    }
                }
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = state.form.costo,
                onValueChange = { cost ->
                    onCostChange(cost)
                },
                label = { Text(stringResource(R.string.costo)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
            )

            if (costoFormateado.isNotEmpty()) {
                Text(
                    text = "${stringResource(R.string.moneda)} $costoFormateado",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = state.form.apartado,
                onValueChange = { newValue ->
                    onApartaChange(newValue)
                },
                label = { Text(stringResource(R.string.apartado)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
            )

            if (apartadpFormateado.isNotEmpty()) {
                Text(
                    text = "${stringResource(R.string.moneda)} $apartadpFormateado",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = state.form.instructora,
                onValueChange = { onInstructorChange(it) },
                label = { Text(stringResource(R.string.instructora)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next
                ),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = state.form.instructoraDesc,
                onValueChange = { onInstructorDescChange(it) },
                label = { Text(stringResource(R.string.instructoraDesc)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next
                ),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable { pickImageLauncherInstr.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (state.form.instructorImageUri == null) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Icon(
                            imageVector = Icons.Outlined.CameraAlt,
                            contentDescription = stringResource(R.string.camera),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(32.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = stringResource(R.string.add_image_placeholder),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = stringResource(R.string.image_details),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }

                } else {
                    SubcomposeAsyncImage(
                        model = state.form.instructorImageUri,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { onExpandedChange(it) }
            ) {
                OutlinedTextField(
                    value = selectedLocation?.name ?: stringResource(R.string.select_location),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.location)) },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        onExpandedChange(!expanded)
                    }
                ) {
                    locations.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item.name) },
                            onClick = {
                                onLocationSelected(item)
                                onExpandedChange(false)
                            }
                        )
                    }
                }
            }

            state.form.temarios.forEachIndexed { index, temario ->
                OutlinedTextField(
                    value = temario,
                    onValueChange = {
                        onTemarioChange(index, it)
                    },
                    label = {
                        Text("${stringResource(R.string.temario)} ${index + 1}")
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Next
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            Spacer(Modifier.height(24.dp))
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