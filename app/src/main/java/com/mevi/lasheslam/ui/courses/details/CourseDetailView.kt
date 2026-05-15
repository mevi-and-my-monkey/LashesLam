package com.mevi.lasheslam.ui.courses.details

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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.mevi.lasheslam.R
import com.mevi.lasheslam.navigation.Screen
import com.mevi.lasheslam.session.SessionManager
import com.mevi.lasheslam.ui.components.ErrorDialog
import com.mevi.lasheslam.ui.components.SuccessDialog
import com.mevi.lasheslam.ui.components.WarningDialog
import com.mevi.lasheslam.ui.favorites.FavoriteType
import com.mevi.lasheslam.ui.home.HomeViewModel
import com.mevi.lasheslam.ui.home.cursos.CourseViewModel
import com.mevi.lasheslam.utils.Constants
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

    val courseStatus by viewModelHome.courseStatusCurse.collectAsState()


    LaunchedEffect(serviceId) {
        viewModel.trackScreen(Screen.ServiceDetails.route)
        viewModel.loadCourseById(serviceId)
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

    if (uiState.isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }
    val status = courseStatus ?: Constants.Course.STATUS_REQUESTED

    Box(
        modifier = modifier
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

            SubcomposeAsyncImage(
                model = uiState.courseDetail.imagen,
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 60.dp)
                    .fillMaxWidth()
                    .aspectRatio(4f / 3f)
                    .clip(androidx.compose.foundation.shape.RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentScale = ContentScale.Fit,
                loading = {
                    Box(
                        Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            )

            Spacer(Modifier.height(20.dp))

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

                CourseContent(
                    titulo = uiState.courseDetail.titulo,
                    descripcion = uiState.courseDetail.descripcion,
                    costoTotal = uiState.courseDetail.costo,
                    costoApartado = uiState.courseDetail.apartado
                )

                Spacer(Modifier.height(16.dp))

                CourseInfoRow(
                    fecha = uiState.courseDetail.fecha,
                    horario = "${uiState.courseDetail.horaIncio} - ${uiState.courseDetail.horaFin}",
                    onLocationClick = {
                        Utilities.openGoogleMaps(
                            context,
                            uiState.courseDetail.lat ?: 0.0,
                            uiState.courseDetail.lng ?: 0.0
                        )
                    }
                )

                Spacer(Modifier.height(16.dp))

                ExpandableSection(title = stringResource(R.string.temarios_title)) {
                    uiState.courseDetail.temarios.forEachIndexed { index, temario ->
                        if (temario.isNotBlank()) {
                            Text(
                                text = "• Día ${index + 1}: $temario",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(Modifier.height(4.dp))
                        }
                    }
                }

                InstructorCard(
                    uiState.courseDetail.instructora,
                    uiState.courseDetail.instructoraDesc,
                    uiState.courseDetail.instructoraImage
                )

                Spacer(Modifier.height(24.dp))


                Text(
                    text = stringResource(R.string.request_info),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        IconButton(
                            onClick = {
                                onOpenFacebook(uiState.facebook ?: "")
                            },
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_facebook),
                                contentDescription = stringResource(R.string.facebook),
                                tint = Color.Unspecified
                            )
                        }

                        IconButton(
                            onClick = {
                                onOpenInstagram(uiState.instagram ?: "")
                            },
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_instagram),
                                contentDescription = stringResource(R.string.instragram),
                                tint = Color.Unspecified
                            )
                        }

                        // WHATSAPP
                        IconButton(
                            onClick = {
                                onOpenWhatsApp(
                                    Utilities.createMessageWhatsApp(
                                        titulo = uiState.courseDetail.titulo,
                                        fecha = uiState.courseDetail.fecha,
                                        horaInicio = uiState.courseDetail.horaIncio,
                                        horaFin = uiState.courseDetail.horaFin,
                                        whatsapp = uiState.whatsApp ?: ""
                                    )
                                )
                            },
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_whatsapp),
                                contentDescription = stringResource(R.string.whatsApp),
                                tint = Color.Unspecified
                            )
                        }
                    }
                }
            }
        }

        // 🔹 TUS BOTONES SUPERIORES – SE RESPETAN COMPLETAMENTE
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (uiState.isAdmin) {
                IconButton(
                    onClick = {
                        onEditClick(serviceId)
                    },
                    modifier = Modifier
                        .size(44.dp)
                        .shadow(4.dp, CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = stringResource(R.string.edit),
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
                        contentDescription = stringResource(R.string.delete),
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
                modifier = Modifier
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
            // 🔥 BOTÓN PRINCIPAL SEGÚN STATUS
            val buttonText = when (status) {
                Constants.Course.STATUS_REQUESTED -> stringResource(R.string.register_course)
                Constants.Course.STATUS_PANDING -> stringResource(R.string.request_pending)
                Constants.Course.STATUS_ACCEPTED -> stringResource(R.string.add_to_calendar)
                else -> stringResource(R.string.register_course)
            }

            val calendarIcon = when (status) {
                Constants.Course.STATUS_ACCEPTED -> Icons.Outlined.EventAvailable
                else -> null
            }

            val isEnabled = status != Constants.Course.STATUS_PANDING

            ElevatedButton(
                onClick = {
                    val uid = SessionManager.currentUserId

                    when (status) {
                        Constants.Course.STATUS_REQUESTED -> {
                            CoroutineScope(Dispatchers.IO).launch {
                                viewModelHome.createCourseRequest(
                                    uid.value ?: "",
                                    serviceId,
                                    uiState.courseDetail.titulo.uppercase(),
                                    uiState.courseDetail.fecha,
                                    "${uiState.courseDetail.horaIncio} - ${uiState.courseDetail.horaFin}",
                                    uiState.nameUser ?: "",
                                    uiState.email ?: ""
                                )
                            }
                        }

                        Constants.Course.STATUS_ACCEPTED -> {
                            onAddToCalendar(
                                uiState.courseDetail.titulo,
                                uiState.courseDetail.fecha,
                                uiState.courseDetail.horaIncio,
                                uiState.courseDetail.horaFin
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
                        contentDescription =  stringResource(R.string.add_to_calendar),
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
                val imageRef = storage.getReferenceFromUrl(uiState.courseDetail.imagen)
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
            title = stringResource(R.string.course_deleted),
            message =stringResource(R.string.course_deleted_message),
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