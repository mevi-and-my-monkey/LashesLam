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
import androidx.compose.ui.text.font.FontWeight
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
    var imageUriInstr by remember { mutableStateOf<Uri?>(null) }
    var costo by remember { mutableStateOf("") }
    var apartado by remember { mutableStateOf("") }
    var nombreInstructora by remember { mutableStateOf("") }
    var descrInstructora by remember { mutableStateOf("") }
    var temarioDiaUno by remember { mutableStateOf("") }
    var temarioDiaDos by remember { mutableStateOf("") }
    var temarioDiaTres by remember { mutableStateOf("") }
    var temarioDiaCuatro by remember { mutableStateOf("") }
    var temarioDiaCinco by remember { mutableStateOf("") }

    val locations by SessionManager.locations.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    var selectedLocation by remember { mutableStateOf<LocationItem?>(null) }

    val costoFormateado = remember(costo) {
        costo.toDoubleOrNull()?.let {
            NumberFormat.getCurrencyInstance(Locale("es", "MX")).format(it)
        } ?: ""
    }

    val apartadpFormateado = remember(apartado) {
        apartado.toDoubleOrNull()?.let {
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

    val pickImageLauncherInstr = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> imageUriInstr = uri }

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
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Icon(
                            imageVector = Icons.Outlined.CameraAlt,
                            contentDescription = "Cámara",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(32.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Añadir imagen de portada",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "(JPG, PNG, Rec: 1200x600px)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }

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
            Spacer(modifier = Modifier.height(8.dp))

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
            Spacer(modifier = Modifier.height(8.dp))

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
            Spacer(modifier = Modifier.height(8.dp))

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
            Spacer(modifier = Modifier.height(8.dp))

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
            Spacer(modifier = Modifier.height(8.dp))

            // Apartar (solo números y formato moneda)
            OutlinedTextField(
                value = apartado,
                onValueChange = { newValue ->
                    // Solo números y punto decimal opcional
                    if (newValue.matches(Regex("""\d*\.?\d*"""))) {
                        apartado = newValue
                    }
                },
                label = { Text("Apartar con") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
            )

            if (apartadpFormateado.isNotEmpty()) {
                Text(
                    text = "MXM: $apartadpFormateado",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Nombre de la instrcutrora (sin mayúscula automática)
            OutlinedTextField(
                value = nombreInstructora,
                onValueChange = { nombreInstructora = it },
                label = { Text("Nombre de la instructora") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    imeAction = ImeAction.Next
                ),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Nombre de la instrcutrora (sin mayúscula automática)
            OutlinedTextField(
                value = descrInstructora,
                onValueChange = { descrInstructora = it },
                label = { Text("Descripcion de la instructora") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    imeAction = ImeAction.Next
                ),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Imagen instructora
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable { pickImageLauncherInstr.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (imageUriInstr == null) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Icon(
                            imageVector = Icons.Outlined.CameraAlt,
                            contentDescription = "Cámara",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(32.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Añadir imagen de portada",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "(JPG, PNG, Rec: 1200x600px)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }

                } else {
                    SubcomposeAsyncImage(
                        model = imageUriInstr,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

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

            OutlinedTextField(
                value = temarioDiaUno,
                onValueChange = { temarioDiaUno = it },
                label = { Text("Temario, dia 1") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    imeAction = ImeAction.Next
                ),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = temarioDiaDos,
                onValueChange = { temarioDiaDos = it },
                label = { Text("Temario, dia 2") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    imeAction = ImeAction.Next
                ),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = temarioDiaTres,
                onValueChange = { temarioDiaTres = it },
                label = { Text("Temario, dia 3") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    imeAction = ImeAction.Next
                ),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = temarioDiaCuatro,
                onValueChange = { temarioDiaCuatro = it },
                label = { Text("Temario, dia 4") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    imeAction = ImeAction.Next
                ),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = temarioDiaCinco,
                onValueChange = { temarioDiaCinco = it },
                label = { Text("Temario, dia 5") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    imeAction = ImeAction.Next
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(Modifier.height(24.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {
                    if (imageUri == null || imageUriInstr == null) return@Button
                    isLoading = true

                    val id = UUID.randomUUID().toString()
                    val courseImageRef = storage.reference.child("services/$id/course.jpg")
                    val instructorImageRef = storage.reference.child("services/$id/instructor.jpg")

                    courseImageRef.putFile(imageUri!!)
                        .continueWithTask { courseImageRef.downloadUrl }
                        .addOnSuccessListener { courseImageUrl ->

                            instructorImageRef.putFile(imageUriInstr!!)
                                .continueWithTask { instructorImageRef.downloadUrl }
                                .addOnSuccessListener { instructorImageUrl ->

                                    val serviceData = mapOf(
                                        "id" to id,
                                        "titulo" to titulo,
                                        "descripcion" to descripcion,
                                        "horaIncio" to horaInicio,
                                        "horaFin" to horaFin,
                                        "fecha" to fecha,
                                        "costo" to costo,
                                        "apartar" to apartado,
                                        "ubicacionNombre" to selectedLocation?.name,
                                        "lat" to selectedLocation?.lat,
                                        "lng" to selectedLocation?.lng,
                                        "instructora" to nombreInstructora,
                                        "instructoraDesc" to descrInstructora,
                                        "diaUno" to temarioDiaUno,
                                        "diaDos" to temarioDiaDos,
                                        "diaTres" to temarioDiaTres,
                                        "diaCuatro" to temarioDiaCuatro,
                                        "diaCinco" to temarioDiaCinco,
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