package com.mevi.lasheslam.ui.courses.edit

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mevi.lasheslam.R
import com.mevi.lasheslam.navigation.Screen
import com.mevi.lasheslam.network.LocationItem
import com.mevi.lasheslam.ui.components.SuccessDialog
import com.mevi.lasheslam.ui.components.pickers.DatePickerDialogCustom
import com.mevi.lasheslam.ui.components.pickers.TimePickerDialog
import com.mevi.lasheslam.ui.courses.edit.components.EditCostView
import com.mevi.lasheslam.ui.courses.edit.components.EditImageView
import com.mevi.lasheslam.ui.courses.edit.components.EditInstructView
import com.mevi.lasheslam.ui.courses.edit.components.EditLocationsView
import com.mevi.lasheslam.ui.courses.edit.components.EditScheduleView
import com.mevi.lasheslam.ui.courses.edit.components.EditTemarioView
import com.mevi.lasheslam.ui.courses.edit.components.EditTitleView
import com.mevi.lasheslam.ui.home.cursos.CourseViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseEditView(
    courseId: String,
    onDismiss: () -> Unit,
    onfinish: () -> Unit,
    viewModel: CourseViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    var expanded by remember { mutableStateOf(false) }
    var selectedLocation by remember { mutableStateOf<LocationItem?>(null) }
    val locations = uiState.locations

    LaunchedEffect(courseId) {
        viewModel.trackScreen(Screen.ServiceEdit.route)
        viewModel.loadCourseById(courseId)
        viewModel.copyToForm()
        selectedLocation = locations.find { it.name == uiState.courseUpdate.ubicacionNombre }
            ?: locations.firstOrNull()
    }

    var showSuccess by remember { mutableStateOf(false) }
    var successMessage by remember { mutableStateOf("") }

    var showTimePickerInicio by remember { mutableStateOf(false) }
    var showTimePickerFin by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.edit_course_title)) },
                navigationIcon = {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ) {

                EditImageView(
                    imageUri = uiState.form.imageUri,
                    imagen = uiState.courseUpdate.imagen,
                    onImageChange = { viewModel.onImageChange(it) },
                )

                Spacer(Modifier.height(16.dp))

                EditTitleView(
                    title = uiState.courseUpdate.titulo,
                    onTitleChange = { viewModel.onTitleChange(it) },
                    description = uiState.courseUpdate.descripcion,
                    onDescriptionChange = { viewModel.onDescriptionChange(it) })

                Spacer(modifier = Modifier.height(8.dp))

                EditScheduleView(
                    hourStart = uiState.courseUpdate.horaIncio,
                    hourEnd = uiState.courseUpdate.horaFin,
                    date = uiState.courseUpdate.fecha,
                    showDatePicker = { showDatePicker = true },
                    showTimePickerInicio = { showTimePickerInicio = true },
                    showTimePickerFin = { showTimePickerFin = true }
                )

                Spacer(modifier = Modifier.height(8.dp))

                EditCostView(
                    uiState.courseUpdate.costo,
                    { viewModel.onCostChange(it) },
                    uiState.courseUpdate.apartar,
                    { viewModel.onApartaChange(it) })

                Spacer(modifier = Modifier.height(8.dp))

                EditInstructView(
                    uiState.courseUpdate.instructora,
                    onInstructorChange = { viewModel.onInstructorChange(it) },
                    uiState.courseUpdate.instructoraDesc,
                    onInstructorDescChange = { viewModel.onInstructorDescChange(it) },
                    uiState.form.instructorImageUri,
                    uiState.courseUpdate.instructoraImage,
                    onInstructorImageChange = { viewModel.onInstructorImageChange(it) }
                )

                Spacer(modifier = Modifier.height(8.dp))

                EditLocationsView(
                    locations,
                    expanded = expanded,
                    selectedLocation = selectedLocation,
                    onExpandedChange = { expanded = it },
                    onLocationSelected = { selectedLocation = it },
                )

                Spacer(modifier = Modifier.height(8.dp))

                EditTemarioView(
                    temarios = uiState.courseUpdate.temarios,
                    onTemarioChange = { index, it ->
                        viewModel.onTemarioChange(index, it)
                    })

                Spacer(Modifier.height(24.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { viewModel.updateCourse(selectedLocation) }
                ) {
                    Text(if (uiState.isLoading) stringResource(R.string.saving) else stringResource(R.string.save_changes) )
                }
            }
        }
    }

    if (showSuccess) {
        SuccessDialog(
            title = stringResource(R.string.update) ,
            message = successMessage,
            onDismiss = {
                showSuccess = false
                onfinish()
            },
            onCancel = {}
        )
    }

    if (showTimePickerInicio) TimePickerDialog(
        onTimeSelected = { viewModel.onHourInitialChange(it) },
        onDismiss = { showTimePickerInicio = false })
    if (showTimePickerFin) TimePickerDialog(
        onTimeSelected = { viewModel.onHourFinalChange(it) },
        onDismiss = { showTimePickerFin = false })
    if (showDatePicker) DatePickerDialogCustom(
        onDateSelected = { viewModel.onDateChange(it) },
        onDismiss = { showDatePicker = false })
}