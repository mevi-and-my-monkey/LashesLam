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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
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
import java.text.NumberFormat
import java.util.Locale
import java.util.UUID

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceAddView(
    onDismiss: () -> Unit,
    linkedBannerIndex: Int,
    firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    storage: FirebaseStorage = FirebaseStorage.getInstance()
) {

    // --- Estados
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var horaInicio by remember { mutableStateOf("") }
    var horaFin by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var costo by remember { mutableStateOf("") }
    val locations by SessionManager.locations.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    var selectedLocation by remember { mutableStateOf<LocationItem?>(null) }

    val costoFormateado = remember(costo) {
        costo.toDoubleOrNull()?.let {
            NumberFormat.getCurrencyInstance(Locale("es", "MX")).format(it)
        } ?: ""
    }

    var isLoading by remember { mutableStateOf(false) }
    var showSuccess by remember { mutableStateOf(false) }
    var successMessage by remember { mutableStateOf("") }

    // Picker modales
    var showTimePickerInicio by remember { mutableStateOf(false) }
    var showTimePickerFin by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> imageUri = uri }

    // --- Scroll adaptado al teclado ---
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Nuevo curso", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(16.dp))

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
                if (imageUri == null) {
                    Text("Seleccionar imagen", color = MaterialTheme.colorScheme.onSurfaceVariant)
                } else {
                    SubcomposeAsyncImage(
                        model = imageUri,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Título (sin mayúscula automática)
            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text("Título") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    imeAction = ImeAction.Next
                ),
                shape = RoundedCornerShape(12.dp)
            )

            // Descripción (multilínea con mayúsculas al inicio)
            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
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

            // Horario (inicio y fin con picker)
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = horaInicio,
                    onValueChange = { horaInicio = it },
                    readOnly = true,
                    label = { Text("Hora inicio") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    trailingIcon = {
                        IconButton(onClick = { showTimePickerInicio = true }) {
                            Icon(Icons.Default.AccessTime, contentDescription = null)
                        }
                    }
                )
                OutlinedTextField(
                    value = horaFin,
                    onValueChange = { horaFin = it },
                    readOnly = true,
                    label = { Text("Hora fin") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    trailingIcon = {
                        IconButton(onClick = { showTimePickerFin = true }) {
                            Icon(Icons.Default.AccessTime, contentDescription = null)
                        }
                    }
                )
            }

            // Fecha (picker con formato dd/MM/yyyy)
            OutlinedTextField(
                value = fecha,
                onValueChange = {},
                readOnly = true,
                label = { Text("Fecha") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.CalendarMonth, contentDescription = null)
                    }
                }
            )

            // Costo (solo números y formato moneda)
            OutlinedTextField(
                value = costo,
                onValueChange = { newValue ->
                    // Solo números y punto decimal opcional
                    if (newValue.matches(Regex("""\d*\.?\d*"""))) {
                        costo = newValue
                    }
                },
                label = { Text("Costo") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
            )

            if (costoFormateado.isNotEmpty()) {
                Text(
                    text = "MXM: $costoFormateado",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Ubicación (primera letra mayúscula)
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedLocation?.name ?: "Seleccionar ubicación",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Ubicación") },
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

            Spacer(Modifier.height(24.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {
                    if (imageUri == null) return@Button
                    isLoading = true

                    val id = UUID.randomUUID().toString()
                    val storageRef = storage.reference.child("services/$id.jpg")
                    storageRef.putFile(imageUri!!)
                        .continueWithTask { storageRef.downloadUrl }
                        .addOnSuccessListener { uri ->
                            val serviceData = mapOf(
                                "id" to id,
                                "titulo" to titulo,
                                "descripcion" to descripcion,
                                "horaIncio" to horaInicio,
                                "horaFin" to horaFin,
                                "fecha" to fecha,
                                "costo" to costo,
                                "ubicacionNombre" to selectedLocation?.name,
                                "lat" to selectedLocation?.lat,
                                "lng" to selectedLocation?.lng,
                                "imagen" to uri.toString(),
                                "banner" to linkedBannerIndex
                            )

                            firestore.collection("data").document("curse")
                                .collection("items").document(id)
                                .set(serviceData)
                                .addOnSuccessListener {
                                    isLoading = false
                                    showSuccess = true
                                    successMessage = "El curso se guardó correctamente."
                                }
                        }
                },
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(if (isLoading) "Guardando..." else "Guardar")
            }

            if (showSuccess) {
                SuccessDialog(
                    title = "Curso agregado",
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

    // --- Pickers de hora ---
    if (showTimePickerInicio) {
        TimePickerDialog(
            onTimeSelected = { horaInicio = it },
            onDismiss = { showTimePickerInicio = false }
        )
    }
    if (showTimePickerFin) {
        TimePickerDialog(
            onTimeSelected = { horaFin = it },
            onDismiss = { showTimePickerFin = false }
        )
    }
    if (showDatePicker) {
        DatePickerDialogCustom(
            onDateSelected = { fecha = it },
            onDismiss = { showDatePicker = false }
        )
    }
}