package com.mevi.lasheslam.ui.home.components

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.outlined.EventAvailable
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.mevi.lasheslam.navigation.Screen
import com.mevi.lasheslam.session.SessionManager
import com.mevi.lasheslam.ui.components.ErrorDialog
import com.mevi.lasheslam.ui.components.SuccessDialog
import com.mevi.lasheslam.ui.components.WarningDialog
import com.mevi.lasheslam.ui.home.HomeViewModel
import com.mevi.lasheslam.utils.Utilities
import androidx.core.net.toUri
import com.mevi.lasheslam.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceDetailView(
    serviceId: String,
    viewModel: HomeViewModel = hiltViewModel(),
    firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    storage: FirebaseStorage = FirebaseStorage.getInstance(),
    onDismiss: () -> Unit,
    navController: NavHostController,
) {
    val isAdmin by SessionManager.isUserAdmin.collectAsState()
    var showConfirmDelete by remember { mutableStateOf(false) }
    var showSuccess by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var warningMessage by remember { mutableStateOf("") }

    val serviceData by viewModel.selectedService.collectAsState()
    val userId = SessionManager.currentUserId.collectAsState().value
    val courseStatus by viewModel.courseStatusCurse.collectAsState()

    // 🔹 Cargar datos solo al entrar
    LaunchedEffect(serviceId) {
        viewModel.loadServiceById(serviceId)
    }

    LaunchedEffect(serviceId) {
        viewModel.loadServiceById(serviceId)
        if (userId != null) {
            viewModel.loadUserCourseStatus(userId, serviceId)
        }
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
    val status = courseStatus ?: "solicitar"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(top = 16.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surface)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 180.dp)
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
                    .fillMaxSize()
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 16.dp,
                        bottom = 120.dp
                    )
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
                    onClick = {
                        navController.navigate(Screen.ServiceEdit.createRoute(serviceId))
                    },
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

        // 🔻 BOTÓN INFERIOR DEPENDIENDO DEL STATUS
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 🔹 Texto superior
            Text(
                text = "Solicitar más información",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // 🔹 Botones circulares (Facebook, Instagram, WhatsApp)
            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {

                // FACEBOOK
                IconButton(
                    onClick = {
                        val url = "https://facebook.com/"
                        navController.context.startActivity(
                            Intent(Intent.ACTION_VIEW, url.toUri())
                        )
                    },
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_facebook),
                        contentDescription = "Facebook",
                        tint = Color.Unspecified
                    )
                }

                // INSTAGRAM
                IconButton(
                    onClick = {
                        val url = "https://instagram.com/"
                        navController.context.startActivity(
                            Intent(Intent.ACTION_VIEW, url.toUri())
                        )
                    },
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_instagram),
                        contentDescription = "Instagram",
                        tint = Color.Unspecified
                    )
                }

                // WHATSAPP
                IconButton(
                    onClick = {
                        val url = "https://wa.me/${SessionManager.whatsApp.value}"
                        navController.context.startActivity(
                            Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        )
                    },
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_whatsapp),
                        contentDescription = "WhatsApp",
                        tint = Color.Unspecified
                    )
                }
            }

            Spacer(Modifier.height(18.dp))

            // 🔥 BOTÓN PRINCIPAL SEGÚN STATUS
            val buttonText = when (status) {
                "solicitar" -> "Solicitar registro"
                "pendiente" -> "Solicitud pendiente"
                "aceptado" -> "Agregar al calendario"
                else -> "Solicitar registro"
            }

            val calendarIcon = when (status) {
                "aceptado" -> Icons.Outlined.EventAvailable
                else -> null
            }

            val isEnabled = status != "pendiente"

            ElevatedButton(
                onClick = {
                    val uid = SessionManager.currentUserId

                    when (status) {
                        "solicitar" -> {
                            CoroutineScope(Dispatchers.IO).launch {
                                viewModel.createCourseRequest(
                                    uid.value ?: "",
                                    serviceId,
                                    titulo.uppercase(),
                                    fecha,
                                    "$horaInicio - $horaFin"
                                )
                            }
                        }

                        "aceptado" -> {
                            Utilities.agregarEventoCalendario(
                                navController = navController,
                                titulo = titulo,
                                fecha = fecha,
                                horaInicio = horaInicio,
                                horaFin = horaFin
                            )
                        }
                    }
                },
                enabled = isEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(55.dp)
            ) {
                if (calendarIcon != null) {
                    Icon(
                        imageVector = calendarIcon,
                        contentDescription = "Agregar al calendario",
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(Modifier.width(10.dp))
                }
                Text(buttonText)
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