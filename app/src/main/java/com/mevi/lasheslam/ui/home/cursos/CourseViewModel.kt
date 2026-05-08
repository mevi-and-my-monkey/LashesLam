package com.mevi.lasheslam.ui.home.cursos

import android.net.Uri
import com.mevi.lasheslam.BaseViewModel
import com.mevi.lasheslam.domain.analytics.AnalyticsEvent
import com.mevi.lasheslam.domain.repository.AnalyticsTracker
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CourseViewModel @Inject constructor(private val analytics: AnalyticsTracker) :
    BaseViewModel<CourseUiState, CourseUiEvent>() {

    override fun createInitialState() = CourseUiState()

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

    fun trackEvent(event: AnalyticsEvent) {
        analytics.track(event)
    }

    fun trackScreen(screen: String) {
        analytics.track(AnalyticsEvent.ScreenView(screen))
    }
}