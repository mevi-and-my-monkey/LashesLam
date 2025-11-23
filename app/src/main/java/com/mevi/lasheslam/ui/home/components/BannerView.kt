package com.mevi.lasheslam.ui.home.components

import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.mevi.lasheslam.navigation.Screen
import com.tbuonomo.viewpagerdotsindicator.compose.DotsIndicator
import com.tbuonomo.viewpagerdotsindicator.compose.model.DotGraphic
import com.tbuonomo.viewpagerdotsindicator.compose.type.ShiftIndicatorType
import com.mevi.lasheslam.session.SessionManager
import com.mevi.lasheslam.ui.components.BottomSheetOption
import com.mevi.lasheslam.ui.components.ErrorDialog
import com.mevi.lasheslam.ui.components.GenericOptionsBottomSheet
import com.mevi.lasheslam.ui.components.SuccessDialog
import com.mevi.lasheslam.ui.home.HomeViewModel
import kotlinx.coroutines.tasks.await

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BannerView(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    navController: NavController
) {
    val firestore = Firebase.firestore
    val storage = FirebaseStorage.getInstance()
    val isAdmin by SessionManager.isUserAdmin.collectAsState()
    var bannerList by remember { mutableStateOf<List<String>>(emptyList()) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var showSuccess by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }
    var showAddView by remember { mutableStateOf(false) }

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.showLoading()
            val storageRef = storage.reference.child("banners/${System.currentTimeMillis()}.jpg")
            val uploadTask = storageRef.putFile(it)
            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) task.exception?.let { e -> throw e }
                storageRef.downloadUrl
            }.addOnSuccessListener { downloadUri ->
                val newUrl = downloadUri.toString()
                firestore.collection("data").document("banners")
                    .update("urls", FieldValue.arrayUnion(newUrl))
                    .addOnSuccessListener {
                        bannerList = bannerList + newUrl
                        viewModel.hideLoading()
                    }
                    .addOnFailureListener {
                        viewModel.hideLoading()
                    }
            }
        }
    }

    // 📡 Cargar banners desde Firestore
    LaunchedEffect(Unit) {
        val snapshot = firestore.collection("data").document("banners").get().await()
        val urls = snapshot.get("urls") as? List<String> ?: emptyList()
        bannerList = urls
    }

    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .wrapContentHeight()
    ) {
        // ---------- Carrusel ----------
        val pagerState = rememberPagerState(pageCount = { bannerList.size })

        HorizontalPager(state = pagerState, pageSpacing = 24.dp) { page ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable {
                        firestore.collection("data").document("curse")
                            .collection("items")
                            .whereEqualTo("banner", page)
                            .get()
                            .addOnSuccessListener { snapshot ->
                                if (!snapshot.isEmpty) {
                                    val doc = snapshot.documents.first()
                                    navController.navigate(Screen.ServiceDetails.createRoute(doc.id))
                                } else if (isAdmin) {
                                    showAddView = true
                                }
                            }
                    },
                contentAlignment = Alignment.Center
            ) {
                if (bannerList.isNotEmpty()) {
                    SubcomposeAsyncImage(
                        model = bannerList[page],
                        contentDescription = "Banner",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit,
                        loading = {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        }
                    )
                } else {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // ---------- Indicador ----------
        if (bannerList.isNotEmpty()) {
            DotsIndicator(
                dotCount = bannerList.size,
                pagerState = pagerState,
                type = ShiftIndicatorType(
                    DotGraphic(color = MaterialTheme.colorScheme.primary, size = 6.dp)
                )
            )
        }

        // ---------- Botón Editar (solo admin) ----------
        AnimatedVisibility(visible = isAdmin) {
            TextButton(
                onClick = { showBottomSheet = true },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Editar", color = MaterialTheme.colorScheme.primary)
            }
        }

        // ---------- BottomSheet ----------
        if (showBottomSheet) {
            GenericOptionsBottomSheet(
                title = "Administrar banners",
                onDismiss = { showBottomSheet = false },
                options = listOf(
                    BottomSheetOption(
                        label = "Subir nuevo banner",
                        icon = Icons.Default.AddPhotoAlternate
                    ) {
                        pickImageLauncher.launch("image/*")
                    },
                    BottomSheetOption(
                        label = "Eliminar banner actual",
                        icon = Icons.Default.Delete
                    ) {
                        val currentBanner = bannerList.getOrNull(pagerState.currentPage)
                        currentBanner?.let { url ->
                            viewModel.showLoading()
                            val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(url)

                            storageRef.delete()
                                .addOnSuccessListener {
                                    firestore.collection("data").document("banners")
                                        .update("urls", FieldValue.arrayRemove(url))
                                        .addOnSuccessListener {
                                            bannerList = bannerList.filterNot { it == url }
                                            viewModel.hideLoading()
                                            showSuccess = true
                                            successMessage = "Banner eliminado correctamente"
                                        }
                                        .addOnFailureListener {
                                            viewModel.hideLoading()
                                            showError = true
                                            errorMessage = "Error al eliminar el banner"
                                        }
                                }
                                .addOnFailureListener {
                                    showError = true
                                    errorMessage = "Error al eliminar el banner"
                                    viewModel.hideLoading()
                                }
                        }
                    }
                )
            )
        }

        if (showSuccess) {
            SuccessDialog(message = successMessage, onDismiss = {
                successMessage = ""
                showSuccess = false
            }, onCancel = {})
        }

        if (showError) {
            ErrorDialog(message = errorMessage, onDismiss = {
                errorMessage = ""
                showError = false
            }, onCancel = {})
        }

        if (showAddView) {
            ServiceAddView(
                linkedBannerIndex = pagerState.currentPage,
                onDismiss = { showAddView = false })
        }
    }
}