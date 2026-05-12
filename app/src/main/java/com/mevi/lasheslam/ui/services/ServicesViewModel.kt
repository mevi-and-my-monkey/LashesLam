package com.mevi.lasheslam.ui.services

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.mevi.lasheslam.BaseViewModel
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.analytics.AnalyticsEvent
import com.mevi.lasheslam.domain.model.CreateServiceModel
import com.mevi.lasheslam.domain.repository.AnalyticsTracker
import com.mevi.lasheslam.domain.usecase.CreateServiceUseCase
import com.mevi.lasheslam.domain.usecase.GetCategoriesServices
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ServicesViewModel @Inject constructor(
    private val getCategoriesServices: GetCategoriesServices,
    private val createServiceUseCase: CreateServiceUseCase,
    private val analytics: AnalyticsTracker
) : BaseViewModel<ServiceUiState, ServiceUiEvent>() {

    override fun createInitialState() = ServiceUiState()

    private var isCategoriesLoaded = false

    fun onTitleChange(title: String) {
        setState { copy(form = form.copy(titulo = title)) }
    }

    fun onSubtitleChange(subtitle: String) {
        setState { copy(form = form.copy(subtitulo = subtitle)) }
    }

    fun onCostChange(cost: Double) {
        setState { copy(form = form.copy(precio = cost)) }
    }

    fun onCategoryChange(categoryId: String) {
        setState { copy(form = form.copy(category = categoryId)) }
    }

    fun onDurationChange(duration: Double) {
        setState { copy(form = form.copy(duracion = duration)) }
    }

    fun onImageChange(image: Uri?) {
        setState { copy(form = form.copy(image = image)) }
    }

    init {
        loadCategories()
    }

    fun loadCategories() {
        if (isCategoriesLoaded) return
        isCategoriesLoaded = true

        viewModelScope.launch {
            getCategoriesServices().collect { result ->
                when (result) {
                    is Resource.Success -> {

                        val sorted = result.data.sortedBy { it.name.lowercase() }

                        setState {
                            copy(categoriesServices = sorted)
                        }
                    }

                    is Resource.Error -> {
                        sendError(result.error) { ServiceUiEvent.ShowError(it) }
                    }
                }
            }
        }
    }

    fun saveService() = launchWithLoading {
        trackEvent(AnalyticsEvent.SaveServiceClick)
        val form = uiState.value.form

        val service = CreateServiceModel(
            duration = form.duracion,
            category = form.category,
            subtitle = form.subtitulo,
            price = form.precio,
            title = form.titulo,
            image = form.image
        )
        when (val result = createServiceUseCase(service)) {
            is Resource.Success -> {
                sendEvent(ServiceUiEvent.ServiceSaved)
            }

            is Resource.Error -> {
                sendEvent(ServiceUiEvent.ShowError(result.error))
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