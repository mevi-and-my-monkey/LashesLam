package com.mevi.lasheslam.ui.home.components

import android.net.Uri
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.mevi.lasheslam.network.LocationItem
import com.mevi.lasheslam.session.SessionManager
import com.mevi.lasheslam.ui.components.SuccessDialog
import com.mevi.lasheslam.ui.components.pickers.DatePickerDialogCustom
import com.mevi.lasheslam.ui.components.pickers.TimePickerDialog
import kotlinx.coroutines.tasks.await
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceEditView(
    serviceId: String,
    onDismiss: () -> Unit,
    onfinish: () -> Unit,
    firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    storage: FirebaseStorage = FirebaseStorage.getInstance()
) {
    var isLoading by remember { mutableStateOf(true) }
    var showSuccess by remember { mutableStateOf(false) }
    var successMessage by remember { mutableStateOf("") }

    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var horaInicio by remember { mutableStateOf("") }
    var horaFin by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    val locations by SessionManager.locations.collectAsState()

    var ubicacionNombre by remember { mutableStateOf("") }
    var lat by remember { mutableStateOf(0.0) }
    var lng by remember { mutableStateOf(0.0) }
    var costo by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var oldImageUrl by remember { mutableStateOf<String?>(null) }

    var selectedLocation by remember { mutableStateOf<LocationItem?>(null) }
    var expandedLocation by remember { mutableStateOf(false) }

    var showTimePickerInicio by remember { mutableStateOf(false) }
    var showTimePickerFin by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val doc = firestore.collection("data").document("curse")
            .collection("items").document(serviceId).get().await()

        val data = doc.data
        if (data != null) {
            titulo = data["titulo"] as? String ?: ""
            descripcion = data["descripcion"] as? String ?: ""
            horaInicio = data["horaIncio"] as? String ?: ""
            horaFin = data["horaFin"] as? String ?: ""
            fecha = data["fecha"] as? String ?: ""
            costo = data["costo"] as? String ?: ""
            oldImageUrl = data["imagen"] as? String
            ubicacionNombre = data["ubicacionNombre"] as? String ?: ""
            lat = data["lat"] as? Double ?: 0.0
            lng = data["lng"] as? Double ?: 0.0
            selectedLocation = locations.find { it.name == ubicacionNombre }
                ?: locations.firstOrNull()
        }
        isLoading = false
    }

    val pickImageLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri -> imageUri = uri }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar curso") },
                navigationIcon = {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->
        if (isLoading) {
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
                // Imagen
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { pickImageLauncher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    when {
                        imageUri != null -> SubcomposeAsyncImage(
                            model = imageUri,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )

                        oldImageUrl != null -> SubcomposeAsyncImage(
                            model = oldImageUrl,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )

                        else -> Text("Seleccionar imagen")
                    }
                }

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text("Título") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 100.dp),
                    shape = RoundedCornerShape(12.dp)
                )

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = horaInicio,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Hora inicio") },
                        modifier = Modifier.weight(1f),
                        trailingIcon = {
                            IconButton(onClick = { showTimePickerInicio = true }) {
                                Icon(Icons.Default.AccessTime, contentDescription = null)
                            }
                        }
                    )

                    OutlinedTextField(
                        value = horaFin,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Hora fin") },
                        modifier = Modifier.weight(1f),
                        trailingIcon = {
                            IconButton(onClick = { showTimePickerFin = true }) {
                                Icon(Icons.Default.AccessTime, contentDescription = null)
                            }
                        }
                    )
                }

                OutlinedTextField(
                    value = fecha,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Fecha") },
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(Icons.Default.CalendarMonth, contentDescription = null)
                        }
                    }
                )

                OutlinedTextField(
                    value = costo,
                    onValueChange = { if (it.matches(Regex("""\\d*\\.?\\d*"""))) costo = it },
                    label = { Text("Costo") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp)
                )

                ExposedDropdownMenuBox(
                    expanded = expandedLocation,
                    onExpandedChange = { expandedLocation = !expandedLocation }
                ) {
                    OutlinedTextField(
                        value = selectedLocation?.name ?: "Seleccionar ubicación",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Ubicación") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedLocation)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = expandedLocation,
                        onDismissRequest = { expandedLocation = false }
                    ) {
                        locations.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(item.name) },
                                onClick = {
                                    selectedLocation = item
                                    expandedLocation = false
                                }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        isLoading = true
                        if (imageUri != null) {
                            val ref = storage.reference.child("services/$serviceId.jpg")
                            ref.putFile(imageUri!!)
                                .continueWithTask { ref.downloadUrl }
                                .addOnSuccessListener { uri ->
                                    saveToFirestore(
                                        serviceId,
                                        uri.toString(),
                                        firestore,
                                        titulo,
                                        descripcion,
                                        horaInicio,
                                        horaFin,
                                        fecha,
                                        selectedLocation,
                                        costo
                                    )
                                    successMessage = "Cambios guardados correctamente"
                                    showSuccess = true
                                }
                        } else {
                            saveToFirestore(
                                serviceId,
                                oldImageUrl,
                                firestore,
                                titulo,
                                descripcion,
                                horaInicio,
                                horaFin,
                                fecha,
                                selectedLocation,
                                costo
                            )
                            successMessage = "Cambios guardados correctamente"
                            showSuccess = true
                        }
                    }
                ) {
                    Text(if (isLoading) "Guardando..." else "Guardar cambios")
                }
            }
        }
    }

    if (showSuccess) {
        SuccessDialog(
            title = "Actualizado",
            message = successMessage,
            onDismiss = {
                showSuccess = false
                onfinish()
            },
            onCancel = {}
        )
    }

    if (showTimePickerInicio) TimePickerDialog(
        onTimeSelected = { horaInicio = it },
        onDismiss = { showTimePickerInicio = false })
    if (showTimePickerFin) TimePickerDialog(
        onTimeSelected = { horaFin = it },
        onDismiss = { showTimePickerFin = false })
    if (showDatePicker) DatePickerDialogCustom(
        onDateSelected = { fecha = it },
        onDismiss = { showDatePicker = false })
}

private fun saveToFirestore(
    id: String,
    imageUrl: String?,
    firestore: FirebaseFirestore,
    titulo: String,
    descripcion: String,
    horaInicio: String,
    horaFin: String,
    fecha: String,
    location: LocationItem?,   // 👈 AQUÍ
    costo: String
) {
    val map = mutableMapOf<String, Any>(
        "titulo" to titulo,
        "descripcion" to descripcion,
        "horaIncio" to horaInicio,
        "horaFin" to horaFin,
        "fecha" to fecha,
        "ubicacionNombre" to (location?.name ?: ""),
        "lat" to (location?.lat ?: 0.0),
        "lng" to (location?.lng ?: 0.0),
        "costo" to costo
    )

    if (imageUrl != null) map["imagen"] = imageUrl

    firestore.collection("data").document("curse")
        .collection("items").document(id)
        .update(map)
}
