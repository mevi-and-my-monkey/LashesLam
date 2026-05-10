package com.mevi.lasheslam.ui.home.cursos

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.mevi.lasheslam.BaseViewModel
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.analytics.AnalyticsEvent
import com.mevi.lasheslam.domain.model.CreateCourseModel
import com.mevi.lasheslam.domain.repository.AnalyticsTracker
import com.mevi.lasheslam.domain.usecase.CreateCourseUseCase
import com.mevi.lasheslam.domain.usecase.GetLocationsUseCase
import com.mevi.lasheslam.network.LocationItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourseViewModel @Inject constructor(
    private val getLocationsUseCase: GetLocationsUseCase,
    private val createCourseUseCase: CreateCourseUseCase,
    private val analytics: AnalyticsTracker
) :
    BaseViewModel<CourseUiState, CourseUiEvent>() {

    override fun createInitialState() = CourseUiState()

    init {
        getLocations()
    }

    fun onTitleChange(title: String) {
        setState { copy(form = form.copy(titulo = title)) }
    }

    fun onDescriptionChange(description: String) {
        setState { copy(form = form.copy(descripcion = description)) }
    }

    fun onHourInitialChange(hour: String) {
        setState { copy(form = form.copy(horaInicio = hour)) }
    }

    fun onHourFinalChange(hour: String) {
        setState { copy(form = form.copy(horaFin = hour)) }
    }

    fun onDateChange(date: String) {
        setState { copy(form = form.copy(fecha = date)) }
    }

    fun onCostChange(cost: String) {
        if (cost.matches(Regex("""\d*\.?\d*"""))) {
            setState { copy(form = form.copy(costo = cost)) }
        }
    }

    fun onApartaChange(aparta: String) {
        if (aparta.matches(Regex("""\d*\.?\d*"""))) {
            setState { copy(form = form.copy(apartado = aparta)) }
        }
    }

    fun onInstructorChange(instructor: String) {
        setState { copy(form = form.copy(instructora = instructor)) }
    }

    fun onInstructorDescChange(instructorDesc: String) {
        setState { copy(form = form.copy(instructoraDesc = instructorDesc)) }
    }

    fun onTemarioChange(index: Int, value: String) {
        val updatedTemarios = uiState.value.form.temarios.toMutableList()
        updatedTemarios[index] = value
        setState { copy(form = form.copy(temarios = updatedTemarios)) }
    }

    fun onImageChange(image: Uri?) {
        setState { copy(form = form.copy(imageUri = image)) }
    }

    fun onInstructorImageChange(image: Uri?) {
        setState { copy(form = form.copy(instructorImageUri = image)) }
    }

    private fun getLocations() {
        viewModelScope.launch {
            getLocationsUseCase().collect { locations ->
                setState {
                    copy(locations = locations)
                }
            }
        }
    }

    fun saveCourse(selectedLocation: LocationItem?, linkedBannerIndex: Int) = launchWithLoading {
        trackEvent(AnalyticsEvent.SaveCourseClick)
        val form = uiState.value.form

        val course = CreateCourseModel(
            titulo = form.titulo,
            descripcion = form.descripcion,
            horaInicio = form.horaInicio,
            horaFin = form.horaFin,
            fecha = form.fecha,
            costo = form.costo,
            apartado = form.apartado,
            instructora = form.instructora,
            instructoraDesc = form.instructoraDesc,
            temarios = form.temarios,
            imageUri = form.imageUri,
            instructorImageUri = form.instructorImageUri,
            ubicacionNombre = selectedLocation?.name,
            lat = selectedLocation?.lat,
            lng = selectedLocation?.lng,
            banner = linkedBannerIndex
        )
        when (val result = createCourseUseCase(course)) {
            is Resource.Success -> {
                sendEvent(CourseUiEvent.CourseSaved)
            }

            is Resource.Error -> {
                sendEvent(CourseUiEvent.ShowError(result.error))
            }
        }

    }

    fun trackEvent(event: AnalyticsEvent) {
        analytics.track(event)
    }

    fun trackScreen(screen: String) {
        analytics.track(AnalyticsEvent.ScreenView(screen))
    }
}