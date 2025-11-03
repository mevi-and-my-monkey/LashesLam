package com.mevi.lasheslam.ui.home.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceAddView(
    onDismiss: () -> Unit,
    linkedBannerIndex: Int,
    firestore: FirebaseFirestore = Firebase.firestore,
    storage: FirebaseStorage = FirebaseStorage.getInstance()
) {
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var horario by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var costo by remember { mutableStateOf("") }
    var ubicacion by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var showSuccess by remember { mutableStateOf(false) }

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> imageUri = uri }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Nuevo Servicio", style = MaterialTheme.typography.titleMedium)
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
            OutlinedTextField(value = titulo, onValueChange = { titulo = it }, label = { Text("Título") })
            OutlinedTextField(value = descripcion, onValueChange = { descripcion = it }, label = { Text("Descripción") })
            OutlinedTextField(value = horario, onValueChange = { horario = it }, label = { Text("Horario") })
            OutlinedTextField(value = fecha, onValueChange = { fecha = it }, label = { Text("Fecha") })
            OutlinedTextField(value = costo, onValueChange = { costo = it }, label = { Text("Costo") })
            OutlinedTextField(value = ubicacion, onValueChange = { ubicacion = it }, label = { Text("Ubicación") })

            Spacer(Modifier.height(24.dp))
            Button(
                onClick = {
                    if (imageUri != null) {
                        isLoading = true
                        val storageRef = storage.reference.child("services/${System.currentTimeMillis()}.jpg")
                        storageRef.putFile(imageUri!!)
                            .continueWithTask { task ->
                                if (!task.isSuccessful) throw task.exception!!
                                storageRef.downloadUrl
                            }
                            .addOnSuccessListener { uri ->
                                val serviceData = mapOf(
                                    "titulo" to titulo,
                                    "descripcion" to descripcion,
                                    "horario" to horario,
                                    "fecha" to fecha,
                                    "costo" to costo,
                                    "ubicacion" to ubicacion,
                                    "imagen" to uri.toString(),
                                    "banner" to linkedBannerIndex,
                                )
                                firestore.collection("data")
                                    .document("service")
                                    .collection("items")
                                    .add(serviceData)
                                    .addOnSuccessListener {
                                        isLoading = false
                                        showSuccess = true
                                    }
                            }
                    }
                },
                enabled = !isLoading
            ) {
                Text("Guardar")
            }

            if (showSuccess) {
                AlertDialog(
                    onDismissRequest = { onDismiss() },
                    confirmButton = {
                        TextButton(onClick = onDismiss) { Text("Cerrar") }
                    },
                    title = { Text("Servicio agregado") },
                    text = { Text("El servicio se ha guardado correctamente.") }
                )
            }
        }
    }
}