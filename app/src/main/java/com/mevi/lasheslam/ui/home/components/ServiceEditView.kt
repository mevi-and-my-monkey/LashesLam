package com.mevi.lasheslam.ui.home.components

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceEditView(
    serviceId: String,
    onDismiss: () -> Unit,
    firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    storage: FirebaseStorage = FirebaseStorage.getInstance()
) {
    var data by remember { mutableStateOf<Map<String, Any>?>(null) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val doc = firestore.collection("data").document("service")
            .collection("items").document(serviceId).get().await()
        data = doc.data
        isLoading = false
    }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        if (isLoading) {
            Box(Modifier
                .fillMaxWidth()
                .padding(24.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (data != null) {
            var titulo by remember { mutableStateOf(data!!["titulo"] as String) }
            var descripcion by remember { mutableStateOf(data!!["descripcion"] as String) }

            Column(
                Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ) {
                Text("Editar servicio", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text("Título") })
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") })

                Spacer(Modifier.height(24.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Button(onClick = {
                        firestore.collection("data").document("service")
                            .collection("items").document(serviceId)
                            .update(mapOf("titulo" to titulo, "descripcion" to descripcion))
                        onDismiss()
                    }) { Text("Guardar cambios") }

                    OutlinedButton(onClick = {
                        val imageUrl = data!!["imagen"] as? String
                        imageUrl?.let {
                            storage.getReferenceFromUrl(it).delete()
                        }
                        firestore.collection("data").document("service")
                            .collection("items").document(serviceId).delete()
                        onDismiss()
                    }) { Text("Eliminar") }
                }
            }
        }
    }
}