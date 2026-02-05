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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.outlined.CameraAlt
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
import kotlinx.coroutines.tasks.await

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
    var apartado by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var imageUriInstr by remember { mutableStateOf<Uri?>(null) }
    var oldImageUrl by remember { mutableStateOf<String?>(null) }
    var oldImageUrlInst by remember { mutableStateOf<String?>(null) }

    var nameInstruc by remember { mutableStateOf("") }
    var descInstruc by remember { mutableStateOf("") }

    var diaUno by remember { mutableStateOf("") }
    var diaDos by remember { mutableStateOf("") }
    var diaTres by remember { mutableStateOf("") }
    var diaCuatro by remember { mutableStateOf("") }
    var diaCinco by remember { mutableStateOf("") }

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
            apartado = data["apartar"] as? String ?: ""
            nameInstruc = data["instructora"] as? String ?: ""
            descInstruc = data["instructoraDesc"] as? String ?: ""
            diaUno = data["diaUno"] as? String ?: ""
            diaDos = data["diaDos"] as? String ?: ""
            diaTres = data["diaTres"] as? String ?: ""
            diaCuatro = data["diaCuatro"] as? String ?: ""
            diaCinco = data["diaCinco"] as? String ?: ""
            oldImageUrlInst = data["instructoraImage"] as? String
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

    val pickImageLauncherInst = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri -> imageUriInstr = uri }

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

                        else -> {
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
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 100.dp),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))

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
                Spacer(modifier = Modifier.height(8.dp))

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
                Spacer(modifier = Modifier.height(8.dp))

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
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp)
                )
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

                Spacer(modifier = Modifier.height(8.dp))

                // Nombre de la instrcutrora (sin mayúscula automática)
                OutlinedTextField(
                    value = nameInstruc,
                    onValueChange = { nameInstruc = it },
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
                    value = descInstruc,
                    onValueChange = { descInstruc = it },
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
                        .clickable { pickImageLauncherInst.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    when {
                        imageUriInstr != null -> SubcomposeAsyncImage(
                            model = imageUriInstr,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )

                        oldImageUrlInst != null -> SubcomposeAsyncImage(
                            model = oldImageUrlInst,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )

                        else -> {
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
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))


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

                OutlinedTextField(
                    value = diaUno,
                    onValueChange = { diaUno = it },
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
                    value = diaDos,
                    onValueChange = { diaDos = it },
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
                    value = diaTres,
                    onValueChange = { diaTres = it },
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
                    value = diaCuatro,
                    onValueChange = { diaCuatro = it },
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
                    value = diaCinco,
                    onValueChange = { diaCinco = it },
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
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        isLoading = true

                        // Función final para guardar en Firestore
                        fun save(courseImage: String?, instructorImage: String?) {
                            saveToFirestore(
                                id = serviceId,
                                imageUrl = courseImage,
                                instructorImageUrl = instructorImage,
                                firestore = firestore,
                                titulo = titulo,
                                descripcion = descripcion,
                                horaInicio = horaInicio,
                                horaFin = horaFin,
                                fecha = fecha,
                                location = selectedLocation,
                                costo = costo,
                                apartar = apartado,
                                instructora = nameInstruc,
                                instructoraDesc = descInstruc,
                                diaUno = diaUno,
                                diaDos = diaDos,
                                diaTres = diaTres,
                                diaCuatro = diaCuatro,
                                diaCinco = diaCinco
                            )

                            successMessage = "Cambios guardados correctamente"
                            showSuccess = true
                            isLoading = false
                        }

                        // --- CASOS ---
                        when {
                            imageUri != null && imageUriInstr != null -> {
                                val courseRef =
                                    storage.reference.child("services/$serviceId/course.jpg")
                                val instrRef =
                                    storage.reference.child("services/$serviceId/instructor.jpg")

                                courseRef.putFile(imageUri!!)
                                    .continueWithTask { courseRef.downloadUrl }
                                    .addOnSuccessListener { courseUrl ->
                                        instrRef.putFile(imageUriInstr!!)
                                            .continueWithTask { instrRef.downloadUrl }
                                            .addOnSuccessListener { instrUrl ->
                                                save(courseUrl.toString(), instrUrl.toString())
                                            }
                                    }
                            }

                            imageUri != null -> {
                                val courseRef =
                                    storage.reference.child("services/$serviceId/course.jpg")
                                courseRef.putFile(imageUri!!)
                                    .continueWithTask { courseRef.downloadUrl }
                                    .addOnSuccessListener { courseUrl ->
                                        save(courseUrl.toString(), oldImageUrlInst)
                                    }
                            }

                            imageUriInstr != null -> {
                                val instrRef =
                                    storage.reference.child("services/$serviceId/instructor.jpg")
                                instrRef.putFile(imageUriInstr!!)
                                    .continueWithTask { instrRef.downloadUrl }
                                    .addOnSuccessListener { instrUrl ->
                                        save(oldImageUrl, instrUrl.toString())
                                    }
                            }

                            else -> {
                                save(oldImageUrl, oldImageUrlInst)
                            }
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
    instructorImageUrl: String?,
    firestore: FirebaseFirestore,
    titulo: String,
    descripcion: String,
    horaInicio: String,
    horaFin: String,
    fecha: String,
    location: LocationItem?,
    costo: String,
    apartar: String,
    instructora: String,
    instructoraDesc: String,
    diaUno: String,
    diaDos: String,
    diaTres: String,
    diaCuatro: String,
    diaCinco: String
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
        "costo" to costo,
        "apartar" to apartar,
        "instructora" to instructora,
        "instructoraDesc" to instructoraDesc,
        "diaUno" to diaUno,
        "diaDos" to diaDos,
        "diaTres" to diaTres,
        "diaCuatro" to diaCuatro,
        "diaCinco" to diaCinco
    )

    imageUrl?.let { map["imagen"] = it }
    instructorImageUrl?.let { map["instructoraImage"] = it }

    firestore.collection("data")
        .document("curse")
        .collection("items")
        .document(id)
        .update(map)
}
