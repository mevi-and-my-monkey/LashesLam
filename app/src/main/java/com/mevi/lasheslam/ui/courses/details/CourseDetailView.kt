package com.mevi.lasheslam.ui.courses.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.mevi.lasheslam.R
import com.mevi.lasheslam.navigation.Screen
import com.mevi.lasheslam.ui.components.ErrorDialog
import com.mevi.lasheslam.ui.components.SuccessDialog
import com.mevi.lasheslam.ui.components.WarningDialog
import com.mevi.lasheslam.ui.home.HomeViewModel
import com.mevi.lasheslam.ui.home.cursos.CourseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseDetailView(
    courseId: String,
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

    LaunchedEffect(courseId) {
        viewModel.trackScreen(Screen.ServiceDetails.route)
        viewModel.loadCourseById(courseId)
    }
    LaunchedEffect(courseId, uiState.currentUserId) {
        if (uiState.currentUserId != null) {
            viewModel.loadUserCourseStatus(courseId)
        }
    }

    if (uiState.isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    CourseDetailContent(
        uiState = uiState,
        courseId = courseId,
        viewModel = viewModel,
        onDismiss = onDismiss,
        modifier = modifier,
        onEditClick = onEditClick,
        onOpenFacebook = onOpenFacebook,
        onOpenInstagram = onOpenInstagram,
        onOpenWhatsApp = onOpenWhatsApp,
        onAddToCalendar = onAddToCalendar,
        onCreateCourseRequest ={
            CoroutineScope(Dispatchers.IO).launch {
                viewModelHome.createCourseRequest(
                    uiState.currentUserId ?: "",
                    courseId,
                    uiState.courseDetail.titulo.uppercase(),
                    uiState.courseDetail.fecha,
                    "${uiState.courseDetail.horaIncio} - ${uiState.courseDetail.horaFin}",
                    uiState.nameUser ?: "",
                    uiState.email ?: ""
                )
            }
        },
        showConfirmDelete = {
            warningMessage = "¿Seguro que deseas eliminar este curso? Esta acción no se puede deshacer."
            showConfirmDelete = true
        }
    )

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
                            .collection("items").document(courseId)
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
            message = stringResource(R.string.course_deleted_message),
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