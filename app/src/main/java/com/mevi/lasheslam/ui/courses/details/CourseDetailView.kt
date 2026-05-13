package com.mevi.lasheslam.ui.courses.details

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.outlined.EventAvailable
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.mevi.lasheslam.R
import com.mevi.lasheslam.session.SessionManager
import com.mevi.lasheslam.ui.components.ErrorDialog
import com.mevi.lasheslam.ui.components.SuccessDialog
import com.mevi.lasheslam.ui.components.WarningDialog
import com.mevi.lasheslam.ui.favorites.FavoriteType
import com.mevi.lasheslam.ui.home.HomeViewModel
import com.mevi.lasheslam.ui.home.cursos.CourseViewModel
import com.mevi.lasheslam.utils.Utilities
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseDetailView(
    serviceId: String,
    viewModelHome: HomeViewModel = hiltViewModel(),
    viewModel: CourseViewModel = hiltViewModel(),
    firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    storage: FirebaseStorage = FirebaseStorage.getInstance(),
    onDismiss: () -> Unit,
    modifier: Modifier,
    onEditClick: (String) -> Unit,
    onOpenFacebook: (String) -> Unit,
    onOpenInstagram: (String) -> Unit,
    onOpenWhatsApp: (String) -> Unit,
    onAddToCalendar: (
        titulo: String,
        fecha: String,
        horaInicio: String,
        horaFin: String
    ) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    var showConfirmDelete by remember { mutableStateOf(false) }
    var showSuccess by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var warningMessage by remember { mutableStateOf("") }
    val isFavorite by viewModelHome.isFavorite
    val context = LocalContext.current

    val serviceData by viewModelHome.selectedService.collectAsState()
    val courseStatus by viewModelHome.courseStatusCurse.collectAsState()

    // 🔹 Cargar datos solo al entrar
    LaunchedEffect(serviceId) {
        viewModelHome.loadServiceById(serviceId)
    }

    LaunchedEffect(serviceId) {
        viewModelHome.loadServiceById(serviceId)
        if (uiState.currentUserId != null) {
            viewModelHome.loadUserCourseStatus(uiState.currentUserId ?: "", serviceId)
        }
    }
    LaunchedEffect(serviceId, uiState.currentUserId) {
        if (uiState.currentUserId != null) {
            viewModelHome.checkIfFavorite(
                uiState.currentUserId ?: "",
                serviceId,
                FavoriteType.COURSE
            )
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
    val apartar = (serviceData?.get("apartar") as? String)?.toInt() ?: 0
    val lat = serviceData?.get("lat") as? Double ?: 0.0
    val lng = serviceData?.get("lng") as? Double ?: 0.0
    val ubicacionNombre = serviceData?.get("ubicacionNombre") as? String ?: ""
    val imagen = serviceData?.get("imagen") as? String ?: ""
    val instructoraDesc = serviceData?.get("instructoraDesc") as? String ?: ""
    val instructora = serviceData?.get("instructora") as? String ?: ""
    val instructoraImage = serviceData?.get("instructoraImage") as? String ?: ""
    val diaUno = serviceData?.get("diaUno") as? String ?: ""
    val diaDos = serviceData?.get("diaDos") as? String ?: ""
    val diaTres = serviceData?.get("diaTres") as? String ?: ""
    val diaCuatro = serviceData?.get("diaCuatro") as? String ?: ""
    val diaCinco = serviceData?.get("diaCinco") as? String ?: ""
    val status = courseStatus ?: "solicitar"

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(top = 16.dp)
    ) {

        Column(
            modifier = Modifier.Companion
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
                modifier = Modifier.Companion
                    .padding(top = 60.dp)
                    .fillMaxWidth()
                    .aspectRatio(4f / 3f)
                    .clip(androidx.compose.foundation.shape.RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentScale = ContentScale.Companion.Fit,
                loading = {
                    Box(
                        Modifier.Companion.fillMaxSize(),
                        contentAlignment = Alignment.Companion.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            )

            Spacer(Modifier.Companion.height(20.dp))

            // 🔥 TARJETA MODERNA
            Column(
                modifier = Modifier.Companion
                    .fillMaxSize()
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 16.dp,
                        bottom = 120.dp
                    )
            ) {

                ServiceContent(
                    titulo = titulo,
                    descripcion = descripcion,
                    costoTotal = costo.toDouble(),
                    costoApartado = apartar.toDouble()
                )

                Spacer(Modifier.Companion.height(16.dp))

                ServiceInfoRow(
                    fecha = fecha,
                    horario = "$horaInicio - $horaFin",
                    onLocationClick = {
                        Utilities.openGoogleMaps(context, lat, lng)
                    }
                )

                Spacer(Modifier.Companion.height(16.dp))

                ExpandableSection(title = "Lo que aprenderás (Temario)") {
                    if (diaUno.isNotEmpty()) Text("• Día 1: $diaUno")
                    Spacer(Modifier.Companion.height(4.dp))
                    if (diaDos.isNotEmpty()) Text("• Día 2: $diaDos")
                    Spacer(Modifier.Companion.height(4.dp))
                    if (diaTres.isNotEmpty()) Text("• Día 3: $diaTres")
                    Spacer(Modifier.Companion.height(4.dp))
                    if (diaCuatro.isNotEmpty()) Text("• Día 4: $diaCuatro")
                    Spacer(Modifier.Companion.height(4.dp))
                    if (diaCinco.isNotEmpty()) Text("• Día 5: $diaCinco")
                }

                InstructorCard(instructora, instructoraDesc, instructoraImage)

                Spacer(Modifier.Companion.height(24.dp))


                // 🔹 Texto superior
                Text(
                    text = "Solicitar más información",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.Companion.padding(bottom = 12.dp)
                )
                Spacer(Modifier.Companion.height(12.dp))

                Row(
                    modifier = Modifier.Companion.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Companion.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        // FACEBOOK
                        IconButton(
                            onClick = {
                                onOpenFacebook(uiState.facebook ?: "")
                            },
                            modifier = Modifier.Companion
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_facebook),
                                contentDescription = "Facebook",
                                tint = Color.Companion.Unspecified
                            )
                        }

                        // INSTAGRAM
                        IconButton(
                            onClick = {
                                onOpenInstagram(uiState.instagram ?: "")
                            },
                            modifier = Modifier.Companion
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
                                onOpenWhatsApp(
                                    Utilities.createMessageWhatsApp(
                                        titulo = titulo,
                                        fecha = fecha,
                                        horaInicio = horaInicio,
                                        horaFin = horaFin,
                                        whatsapp = uiState.whatsApp ?: ""
                                    )
                                )
                            },
                            modifier = Modifier.Companion
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_whatsapp),
                                contentDescription = "WhatsApp",
                                tint = Color.Companion.Unspecified
                            )
                        }
                    }
                }
            }
        }

        // 🔹 TUS BOTONES SUPERIORES – SE RESPETAN COMPLETAMENTE
        Row(
            modifier = Modifier.Companion
                .align(Alignment.Companion.TopEnd)
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (uiState.isAdmin) {
                IconButton(
                    onClick = {
                        onEditClick(serviceId)
                    },
                    modifier = Modifier.Companion
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
                    modifier = Modifier.Companion
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
                onClick = {
                    uiState.currentUserId?.let {
                        viewModelHome.toggleFavorite(
                            it,
                            serviceId,
                            FavoriteType.COURSE
                        )
                    }
                },
                modifier = Modifier.Companion
                    .size(44.dp)
                    .shadow(4.dp, CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = "Favorito",
                    tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            IconButton(
                onClick = onDismiss,
                modifier = Modifier.Companion
                    .size(44.dp)
                    .shadow(4.dp, CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
            ) {
                Icon(Icons.Default.Close, contentDescription = "Cerrar")
            }
        }

        // 🔻 BOTÓN INFERIOR DEPENDIENDO DEL STATUS
        Column(
            modifier = Modifier.Companion
                .align(Alignment.Companion.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.Companion.CenterHorizontally
        ) {
            // 🔥 BOTÓN PRINCIPAL SEGÚN STATUS
            val buttonText = when (status) {
                "solicitar" -> "Registrar"
                "pendiente" -> "Solicitud pendiente"
                "aceptado" -> "Agregar al calendario"
                else -> "Registrar"
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
                                viewModelHome.createCourseRequest(
                                    uid.value ?: "",
                                    serviceId,
                                    titulo.uppercase(),
                                    fecha,
                                    "$horaInicio - $horaFin",
                                    uiState.nameUser ?: "",
                                    uiState.email ?: ""
                                )
                            }
                        }

                        "aceptado" -> {
                            onAddToCalendar(
                                titulo,
                                fecha,
                                horaInicio,
                                horaFin
                            )
                        }
                    }
                },
                enabled = isEnabled,
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(55.dp)
            ) {
                if (calendarIcon != null) {
                    Icon(
                        imageVector = calendarIcon,
                        contentDescription = "Agregar al calendario",
                        modifier = Modifier.Companion.size(22.dp)
                    )
                    Spacer(Modifier.Companion.width(10.dp))
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
fun ServiceContent(
    titulo: String,
    descripcion: String,
    costoTotal: Double,
    costoApartado: Double
) {
    Column {
        Text(
            text = titulo.uppercase(),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Companion.Bold
        )

        Spacer(Modifier.Companion.height(8.dp))

        Text(
            buildAnnotatedString {
                append("Costo Total: ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Companion.Bold)) {
                    append("$${costoTotal} MXN")
                }
                if (costoApartado != 0.0) {
                    append(" / Aparta con: ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Companion.Bold)) {
                        append("$${costoApartado} MXN")
                    }
                }
            },
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(Modifier.Companion.height(16.dp))

        Text(
            text = descripcion,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun ServiceInfoRow(
    fecha: String,
    horario: String,
    onLocationClick: () -> Unit
) {
    Row(
        modifier = Modifier.Companion.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Companion.CenterVertically,
    ) {
        InfoIcon(icon = Icons.Default.CalendarToday, text = fecha)
        InfoIcon(icon = Icons.Default.Schedule, text = horario)

        TextButton(onClick = onLocationClick) {
            Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color(0xFFD9869A))
            Spacer(Modifier.Companion.width(4.dp))
            Text("Ver ubicación", color = Color(0xFFD9869A), fontWeight = FontWeight.Companion.Bold)
        }
    }
}

@Composable
private fun InfoIcon(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.Companion.CenterVertically) {
        Icon(icon, contentDescription = null, modifier = Modifier.Companion.size(20.dp))
        Spacer(modifier = Modifier.Companion.width(8.dp))
        Text(text, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun ExpandableSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.Companion
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(androidx.compose.foundation.shape.RoundedCornerShape(12.dp))
            .background(Color.Companion.White)
            .border(
                1.dp,
                Color(0xFFF0F0F0),
                androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
            )
            .animateContentSize()
            .clickable { expanded = !expanded }
    ) {
        Row(
            modifier = Modifier.Companion.padding(16.dp),
            verticalAlignment = Alignment.Companion.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Companion.Bold,
                modifier = Modifier.Companion.weight(1f)
            )
            Icon(
                imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = if (expanded) "Colapsar" else "Expandir"
            )
        }
        if (expanded) {
            Column(
                modifier = Modifier.Companion
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            ) {
                content()
            }
        }
    }
}

@Composable
fun InstructorCard(name: String, description: String = "", image: String) {
    Column(modifier = Modifier.Companion.padding(vertical = 16.dp)) {
        Text(
            "Instructor(a)",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Companion.Bold
        )
        Spacer(modifier = Modifier.Companion.height(16.dp))
        Row(verticalAlignment = Alignment.Companion.CenterVertically) {
            SubcomposeAsyncImage(
                model = image,
                contentDescription = name,
                modifier = Modifier.Companion
                    .size(80.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Companion.Crop,
                loading = { CircularProgressIndicator() }
            )
            Spacer(modifier = Modifier.Companion.width(16.dp))
            Column {
                Text(
                    name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Companion.Bold
                )
                Text(
                    description,
                    style = MaterialTheme.typography.bodyMedium,
                    lineHeight = 18.sp
                )
            }
        }
    }
}