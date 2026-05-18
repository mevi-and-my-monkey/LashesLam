package com.mevi.lasheslam.ui.courses.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mevi.lasheslam.R
import com.mevi.lasheslam.ui.courses.details.components.DetailBotonStatusView
import com.mevi.lasheslam.ui.courses.details.components.DetailCostView
import com.mevi.lasheslam.ui.courses.details.components.DetailDescriptionView
import com.mevi.lasheslam.ui.courses.details.components.DetailUbicationView
import com.mevi.lasheslam.ui.courses.details.components.DetailsImageView
import com.mevi.lasheslam.ui.courses.details.components.DetailsOptionsTop
import com.mevi.lasheslam.ui.courses.details.components.DetailsSocialMediaView
import com.mevi.lasheslam.ui.courses.details.components.DetailsTemarioView
import com.mevi.lasheslam.ui.favorites.FavoriteType
import com.mevi.lasheslam.ui.home.cursos.CourseUiState
import com.mevi.lasheslam.ui.home.cursos.CourseViewModel
import com.mevi.lasheslam.utils.Utilities

@Composable
fun CourseDetailContent(
    uiState: CourseUiState,
    courseId: String,
    viewModel: CourseViewModel = hiltViewModel(),
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
    onCreateCourseRequest: () -> Unit,
    showConfirmDelete: () -> Unit,

    ) {
    val context = LocalContext.current
    val status = uiState.courseStatus
    val favoritesList = viewModel.favorites.collectAsState().value

    val isFavorite = remember(favoritesList, courseId) {
        favoritesList.any { it.itemId == courseId && it.type == FavoriteType.COURSE.name }
    }
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
            DetailsImageView(uiState.courseDetail.imagen)

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

                CourseContent(titulo = uiState.courseDetail.titulo)

                DetailCostView(
                    costoTotal = uiState.courseDetail.costo,
                    costoApartado = uiState.courseDetail.apartado,
                    horaInicio = uiState.courseDetail.horaIncio,
                    horaFin = uiState.courseDetail.horaFin
                )

                DetailUbicationView(
                    fecha = uiState.courseDetail.fecha,
                    location = uiState.courseDetail.ubicacionNombre,
                    onLocationClick = {
                        Utilities.openGoogleMaps(
                            context,
                            uiState.courseDetail.lat ?: 0.0,
                            uiState.courseDetail.lng ?: 0.0
                        )
                    }
                )

                Spacer(Modifier.height(8.dp))

                DetailDescriptionView(descripcion = uiState.courseDetail.descripcion)

                Spacer(Modifier.height(16.dp))

                DetailsTemarioView(uiState.courseDetail.temarios)

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

                DetailsSocialMediaView(
                    onOpenFacebook,
                    onOpenInstagram,
                    onOpenWhatsApp,
                    uiState.facebook ?: "",
                    uiState.instagram ?: "",
                    uiState.whatsApp ?: "",
                    titulo = uiState.courseDetail.titulo,
                    fecha = uiState.courseDetail.fecha,
                    horaInicio = uiState.courseDetail.horaIncio,
                    horaFin = uiState.courseDetail.horaFin,

                    )
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DetailsOptionsTop(
                uiState.isAdmin,
                isFavorite,
                courseId,
                onEditClick,
                showConfirmDelete,
                onDismiss,
                toggleFavorite = { id ->
                    viewModel.toggleFavorite(id, FavoriteType.COURSE)
                }
            )
        }


        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DetailBotonStatusView(status, onAddToCalendar = {
                onAddToCalendar(
                    uiState.courseDetail.titulo,
                    uiState.courseDetail.fecha,
                    uiState.courseDetail.horaIncio,
                    uiState.courseDetail.horaFin
                )
            }, onCreateCourseRequest)

        }
    }

}