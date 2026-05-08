package com.mevi.lasheslam.ui.home.components

import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.mevi.lasheslam.R
import com.mevi.lasheslam.network.LocationItem
import com.mevi.lasheslam.session.SessionManager
import com.mevi.lasheslam.ui.components.SuccessDialog
import com.mevi.lasheslam.ui.components.pickers.DatePickerDialogCustom
import com.mevi.lasheslam.ui.components.pickers.TimePickerDialog
import com.mevi.lasheslam.ui.home.cursos.CourseViewModel
import com.mevi.lasheslam.utils.Constants
import java.text.NumberFormat
import java.util.Locale
import java.util.UUID

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseAddView(
    onDismiss: () -> Unit,
    linkedBannerIndex: Int,
    firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    storage: FirebaseStorage = FirebaseStorage.getInstance(),
    viewModel: CourseViewModel = hiltViewModel()
) {

    val state by viewModel.uiState.collectAsState()

    val locations by SessionManager.locations.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    var selectedLocation by remember { mutableStateOf<LocationItem?>(null) }

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

    var isLoading by remember { mutableStateOf(false) }
    var showSuccess by remember { mutableStateOf(false) }
    var successMessage by remember { mutableStateOf("") }

    var showTimePickerInicio by remember { mutableStateOf(false) }
    var showTimePickerFin by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> viewModel.onImageChange(uri) }

    val pickImageLauncherInstr = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> viewModel.onInstructorImageChange(uri) }

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
                onValueChange = { viewModel.onTitleChange(it) },
                label = { Text(stringResource(R.string.title)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    imeAction = ImeAction.Next
                ),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = state.form.descripcion,
                onValueChange = { viewModel.onDescriptionChange(it) },
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
                    onValueChange = { viewModel.onHourInitialChange(it) },
                    readOnly = true,
                    label = { Text(stringResource(R.string.hora_inicio)) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    trailingIcon = {
                        IconButton(onClick = { showTimePickerInicio = true }) {
                            Icon(Icons.Default.AccessTime, contentDescription = null)
                        }
                    }
                )
                OutlinedTextField(
                    value = state.form.horaFin,
                    onValueChange = { viewModel.onHourFinalChange(it) },
                    readOnly = true,
                    label = { Text(stringResource(R.string.hora_fin)) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    trailingIcon = {
                        IconButton(onClick = { showTimePickerFin = true }) {
                            Icon(Icons.Default.AccessTime, contentDescription = null)
                        }
                    }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = state.form.fecha,
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(R.string.fecha)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.CalendarMonth, contentDescription = null)
                    }
                }
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = state.form.costo,
                onValueChange = { cost ->
                    viewModel.onCostChange(cost)
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
                    viewModel.onApartaChange(newValue)
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
                onValueChange = { viewModel.onInstructorChange(it) },
                label = { Text(stringResource(R.string.instructora)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    imeAction = ImeAction.Next
                ),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = state.form.instructoraDesc,
                onValueChange = { viewModel.onInstructorDescChange(it) },
                label = { Text(stringResource(R.string.instructoraDesc)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
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
                onExpandedChange = { expanded = !expanded }
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
                    onDismissRequest = { expanded = false }
                ) {
                    locations.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item.name) },
                            onClick = {
                                selectedLocation = item
                                expanded = false
                            }
                        )
                    }
                }
            }

            state.form.temarios.forEachIndexed { index, temario ->
                OutlinedTextField(
                    value = temario,
                    onValueChange = {
                        viewModel.onTemarioChange(index, it)
                    },
                    label = {
                        Text("${stringResource(R.string.temario)} ${index + 1}")
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.None,
                        imeAction = ImeAction.Next
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            Spacer(Modifier.height(24.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {
                    if (state.form.imageUri == null || state.form.instructorImageUri == null) return@Button
                    isLoading = true

                    val id = UUID.randomUUID().toString()
                    val courseImageRef = storage.reference.child("services/$id/course.jpg")
                    val instructorImageRef = storage.reference.child("services/$id/instructor.jpg")

                    courseImageRef.putFile(state.form.imageUri!!)
                        .continueWithTask { courseImageRef.downloadUrl }
                        .addOnSuccessListener { courseImageUrl ->

                            instructorImageRef.putFile(state.form.instructorImageUri!!)
                                .continueWithTask { instructorImageRef.downloadUrl }
                                .addOnSuccessListener { instructorImageUrl ->

                                    val serviceData = mapOf(
                                        "id" to id,
                                        "titulo" to state.form.titulo,
                                        "descripcion" to state.form.descripcion,
                                        "horaIncio" to state.form.horaInicio,
                                        "horaFin" to state.form.horaFin,
                                        "fecha" to state.form.fecha,
                                        "costo" to state.form.costo,
                                        "apartar" to state.form.apartado,
                                        "ubicacionNombre" to selectedLocation?.name,
                                        "lat" to selectedLocation?.lat,
                                        "lng" to selectedLocation?.lng,
                                        "instructora" to state.form.instructora,
                                        "instructoraDesc" to state.form.instructoraDesc,
                                        "diaUno" to state.form.temarios[0],
                                        "diaDos" to state.form.temarios[1],
                                        "diaTres" to state.form.temarios[2],
                                        "diaCuatro" to state.form.temarios[3],
                                        "diaCinco" to state.form.temarios[4],
                                        "imagen" to courseImageUrl.toString(),
                                        "instructoraImage" to instructorImageUrl.toString(),
                                        "banner" to linkedBannerIndex
                                    )

                                    firestore.collection("data")
                                        .document("curse")
                                        .collection("items")
                                        .document(id)
                                        .set(serviceData)
                                        .addOnSuccessListener {
                                            isLoading = false
                                            showSuccess = true
                                            successMessage = "El curso se guardó correctamente."
                                        }
                                }
                        }
                },
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(if (isLoading) stringResource(R.string.saving) else stringResource(R.string.save))
            }

            if (showSuccess) {
                SuccessDialog(
                    title = stringResource(R.string.curso_agregado),
                    message = successMessage,
                    onDismiss = {
                        successMessage = ""
                        showSuccess = false
                        onDismiss()
                    },
                    onCancel = {}
                )
            }
        }
    }

    if (showTimePickerInicio) {
        TimePickerDialog(
            onTimeSelected = { viewModel.onHourInitialChange(it) },
            onDismiss = { showTimePickerInicio = false }
        )
    }
    if (showTimePickerFin) {
        TimePickerDialog(
            onTimeSelected = { viewModel.onHourFinalChange(it) },
            onDismiss = { showTimePickerFin = false }
        )
    }
    if (showDatePicker) {
        DatePickerDialogCustom(
            onDateSelected = { viewModel.onDateChange(it) },
            onDismiss = { showDatePicker = false }
        )
    }
}