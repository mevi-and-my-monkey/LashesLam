package com.mevi.lasheslam.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.mevi.lasheslam.session.SessionManager
import com.mevi.lasheslam.ui.components.ErrorDialog
import com.mevi.lasheslam.ui.components.SuccessDialog
import com.mevi.lasheslam.ui.components.WarningDialog
import com.mevi.lasheslam.ui.home.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceDetailView(
    serviceId: String,
    viewModel: HomeViewModel = hiltViewModel(),
    firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    storage: FirebaseStorage = FirebaseStorage.getInstance(),
    onDismiss: () -> Unit,
) {
    val isAdmin by SessionManager.isUserAdmin.collectAsState()
    var showConfirmDelete by remember { mutableStateOf(false) }
    var showSuccess by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var warningMessage by remember { mutableStateOf("") }

    val serviceData by viewModel.selectedService.collectAsState()

    // 🔹 Cargar datos solo al entrar
    LaunchedEffect(serviceId) {
        viewModel.loadServiceById(serviceId)
    }

    // 🔸 Mostrar cargando mientras llega el snapshot
    if (serviceData == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val titulo = serviceData?.get("titulo") as? String ?: ""
    val descripcion = serviceData?.get("descripcion") as? String ?: ""
    val horaInicio = serviceData?.get("horaIncio") as? String ?: ""
    val horaFin = serviceData?.get("horaFin") as? String ?: ""
    val fecha = serviceData?.get("fecha") as? String ?: ""
    val costo = (serviceData?.get("costo") as? String)?.toInt() ?: 0
    val ubicacion = serviceData?.get("ubicacion") as? String ?: ""
    val imagen = serviceData?.get("imagen") as? String ?: ""

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surface)
                .verticalScroll(rememberScrollState())
        ) {

            // 🔥 Imagen principal (respeta tu diseño)
            SubcomposeAsyncImage(
                model = imagen,
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 60.dp)
                    .fillMaxWidth()
                    .aspectRatio(4f / 3f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentScale = ContentScale.Fit,
                loading = {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            )

            Spacer(Modifier.height(20.dp))

            // 🔥 TARJETA MODERNA
            Column(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f))
                    .padding(20.dp)
            ) {

                // ⭐ TÍTULO CENTRADO Y MAYÚSCULA
                Text(
                    text = titulo.uppercase(),
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(12.dp))

                // 📝 Descripción moderna
                Text(
                    text = descripcion,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )

                Spacer(Modifier.height(24.dp))


                // 🔹 INFO ITEM MODERNO
                InfoItem(icon = "📅", label = "FECHA: ", value = fecha)
                InfoItem(icon = "🕒", label = "HORARIO: ", value = "$horaInicio - $horaFin")
                InfoItem(icon = "💰", label = "COSTO: ", value = "$costo MXN")
                InfoItem(icon = "📍", label = "UBICACIÓN: ", value = ubicacion)

                Spacer(Modifier.height(80.dp))
            }
        }

        // 🔹 TUS BOTONES SUPERIORES – SE RESPETAN COMPLETAMENTE
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (isAdmin) {
                IconButton(
                    onClick = { /* editar */ },
                    modifier = Modifier
                        .size(44.dp)
                        .shadow(4.dp, CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Editar",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                IconButton(
                    onClick = {
                        warningMessage =
                            "¿Seguro que deseas eliminar este curso? Esta acción no se puede deshacer."
                        showConfirmDelete = true
                    },
                    modifier = Modifier
                        .size(44.dp)
                        .shadow(4.dp, CircleShape)
                        .background(MaterialTheme.colorScheme.errorContainer, CircleShape)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }

            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .size(44.dp)
                    .shadow(4.dp, CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
            ) {
                Icon(Icons.Default.Close, contentDescription = "Cerrar")
            }
        }
    }

    if (showConfirmDelete) {
        WarningDialog(
            message = warningMessage,
            onDismiss = {
                showConfirmDelete = false
                warningMessage = ""
                val imageRef = storage.getReferenceFromUrl(imagen)
                imageRef.delete()
                    .addOnSuccessListener {
                        firestore.collection("data").document("curse")
                            .collection("items").document(serviceId)
                            .delete()
                            .addOnSuccessListener { showSuccess = true }
                            .addOnFailureListener {
                                errorMessage = "Error al eliminar datos de Firestore"
                                showError = true
                            }
                    }
                    .addOnFailureListener {
                        errorMessage = "Error al eliminar la imagen del almacenamiento"
                        showError = true
                    }
            },
            onCancel = {
                showConfirmDelete = false
                warningMessage = ""
            }
        )
    }

    if (showSuccess) {
        SuccessDialog(
            title = "Curso eliminado",
            message = "El curso se eliminó correctamente.",
            onDismiss = {
                showSuccess = false
                onDismiss()
            },
            onCancel = {}
        )
    }

    if (showError) {
        ErrorDialog(
            message = errorMessage,
            onDismiss = { showError = false },
            onCancel = {}
        )
    }
}

@Composable
private fun InfoItem(icon: String, label: String, value: String) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = icon, fontSize = MaterialTheme.typography.titleMedium.fontSize)
            Spacer(Modifier.width(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(Modifier.height(12.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}