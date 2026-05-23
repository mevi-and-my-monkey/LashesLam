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
import com.mevi.lasheslam.domain.usecase.service.DeleteServiceUseCase
import com.mevi.lasheslam.domain.usecase.service.GetAServiceDetailUseCase
import com.mevi.lasheslam.domain.usecase.service.UpdateServiceUseCase
import com.mevi.lasheslam.domain.usecase.session.GetFacebookUseCase
import com.mevi.lasheslam.domain.usecase.session.GetInstagramUseCase
import com.mevi.lasheslam.domain.usecase.session.GetWhatsAppUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ServicesViewModel @Inject constructor(
    private val getCategoriesServices: GetCategoriesServices,
    private val getAServiceDetailUseCase: GetAServiceDetailUseCase,
    private val createServiceUseCase: CreateServiceUseCase,
    private val updateServiceUseCase: UpdateServiceUseCase,
    private val deleteServiceUseCase: DeleteServiceUseCase,
    private val getFacebookUseCase: GetFacebookUseCase,
    private val getInstagramUseCase: GetInstagramUseCase,
    private val getWhatsAppUseCase: GetWhatsAppUseCase,
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

    fun onCostChange(cost: String) {
        setState { copy(form = form.copy(precio = cost)) }
    }

    fun onCategoryChange(categoryId: String) {
        setState { copy(form = form.copy(category = categoryId)) }
    }

    fun onDurationChange(duration: String) {
        setState { copy(form = form.copy(duracion = duration)) }
    }

    fun onImageChange(image: Uri?) {
        setState { copy(form = form.copy(image = image)) }
    }

    init {
        observeSession()
        loadCategories()
    }


    private fun observeSession() {
        viewModelScope.launch {
            combine(
                getFacebookUseCase(),
                getInstagramUseCase(),
                getWhatsAppUseCase()
            ) { facebook, instagram, whatsApp ->
                Triple(facebook, instagram, whatsApp)
            }.collect { (facebook, instagram, whatsApp) ->
                setState {
                    copy(
                        facebook = facebook,
                        instagram = instagram,
                        whatsApp = whatsApp
                    )
                }
            }
        }
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
            duration = form.duracion.toDoubleOrNull() ?: 0.0,
            category = form.category,
            subtitle = form.subtitulo,
            price = form.precio.toDoubleOrNull() ?: 0.0,
            title = form.titulo,
            image = form.image,
            id = form.id,
            currentImageUrl = form.remoteImage
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

    fun updateService() = launchWithLoading {
        trackEvent(AnalyticsEvent.UpdateServiceClick)

        val form = uiState.value.form

        val service = CreateServiceModel(
            duration = form.duracion.toDoubleOrNull() ?: 0.0,
            category = form.category,
            subtitle = form.subtitulo,
            price = form.precio.toDoubleOrNull() ?: 0.0,
            title = form.titulo,
            image = form.image,
            id = form.id,
            currentImageUrl = form.remoteImage
        )

        when (val result = updateServiceUseCase(service)) {

            is Resource.Success -> {
                sendEvent(ServiceUiEvent.ServiceUpdated)
            }

            is Resource.Error -> {
                sendEvent(ServiceUiEvent.ShowError(result.error))
            }
        }
    }

    fun deleteService(serviceId: String, imageUrl: String) {
        viewModelScope.launch {
            when (val result = deleteServiceUseCase(serviceId, imageUrl)) {
                is Resource.Success -> {
                    sendEvent(ServiceUiEvent.ServiceDeleted)
                }

                is Resource.Error -> {
                    sendEvent(ServiceUiEvent.ShowError(result.error))
                }
            }
        }
    }

    fun loadServiceById(serviceId: String) = launchWithLoading {
        when (val result = getAServiceDetailUseCase(serviceId)) {
            is Resource.Success -> {
                val product = result.data
                setState {
                    copy(
                        form = form.copy(
                            id = product.id,
                            titulo = product.title,
                            precio = product.price.toString(),
                            subtitulo = product.subtitle,
                            duracion = product.duration.toString(),
                            category = product.category,
                            remoteImage = product.image
                        )
                    )
                }
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