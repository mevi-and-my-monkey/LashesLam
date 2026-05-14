package com.mevi.lasheslam.ui.home.cursos

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.mevi.lasheslam.BaseViewModel
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.analytics.AnalyticsEvent
import com.mevi.lasheslam.domain.model.CreateCourseModel
import com.mevi.lasheslam.domain.model.SessionData
import com.mevi.lasheslam.domain.repository.AnalyticsTracker
import com.mevi.lasheslam.domain.usecase.CreateCourseUseCase
import com.mevi.lasheslam.domain.usecase.GetCurrentUserIdUseCase
import com.mevi.lasheslam.domain.usecase.GetFavoritesUseCase
import com.mevi.lasheslam.domain.usecase.GetIsAdminUseCase
import com.mevi.lasheslam.domain.usecase.GetIsUserInvitedUseCase
import com.mevi.lasheslam.domain.usecase.GetLocationsUseCase
import com.mevi.lasheslam.domain.usecase.GetNameUserUseCase
import com.mevi.lasheslam.domain.usecase.courses.GetACourseDetailUseCase
import com.mevi.lasheslam.domain.usecase.session.GetEmailUserUseCase
import com.mevi.lasheslam.domain.usecase.session.GetFacebookUseCase
import com.mevi.lasheslam.domain.usecase.session.GetInstagramUseCase
import com.mevi.lasheslam.domain.usecase.session.GetWhatsAppUseCase
import com.mevi.lasheslam.network.FavoriteItem
import com.mevi.lasheslam.network.LocationItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourseViewModel @Inject constructor(
    private val getIsAdminUseCase: GetIsAdminUseCase,
    private val getIsUserInvitedUseCase: GetIsUserInvitedUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val getNameUserUseCase: GetNameUserUseCase,
    private val getEmailUserUseCase: GetEmailUserUseCase,
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val getFacebookUseCase: GetFacebookUseCase,
    private val getInstagramUseCase: GetInstagramUseCase,
    private val getWhatsAppUseCase: GetWhatsAppUseCase,
    private val getLocationsUseCase: GetLocationsUseCase,
    private val createCourseUseCase: CreateCourseUseCase,
    private val getACourseDetailUseCase: GetACourseDetailUseCase,
    private val analytics: AnalyticsTracker
) :
    BaseViewModel<CourseUiState, CourseUiEvent>() {

    override fun createInitialState() = CourseUiState()

    private val _favorites = MutableStateFlow<List<FavoriteItem>>(emptyList())
    val favorites = _favorites.asStateFlow()

    init {
        observeSession()
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

    private fun observeSession() {
        viewModelScope.launch {
            combine(
                getIsAdminUseCase(),
                getIsUserInvitedUseCase(),
                getCurrentUserIdUseCase()
            ) { isAdmin, isInvited, userId ->
                SessionData(isAdmin, isInvited, userId ?: "")
            }.collect { (isAdmin, isInvited, userId) ->
                setState {
                    copy(
                        isAdmin = isAdmin,
                        isUserInvited = isInvited,
                        currentUserId = userId
                    )
                }
                if (userId.isNotEmpty()) {
                    loadFavorites(userId)
                }
                if (isAdmin) {
                    //loadAdminPendingRequests()
                } else if (userId.isNotEmpty()) {
                    //observeUserCourses(userId)
                }
            }
        }

        viewModelScope.launch {
            combine(
                getNameUserUseCase(),
                getEmailUserUseCase()
            ) { name, email ->
                name to email
            }.collect { (name, email) ->
                setState {
                    copy(
                        nameUser = name,
                        email = email,
                    )
                }
            }
        }

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

    private fun getLocations() {
        viewModelScope.launch {
            getLocationsUseCase().collect { locations ->
                setState {
                    copy(locations = locations)
                }
            }
        }
    }

    fun loadFavorites(userId: String) {
        viewModelScope.launch {
            when (val result = getFavoritesUseCase(userId)) {
                is Resource.Success -> {
                    _favorites.value = result.data
                }

                else -> {}
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

    fun loadCourseById(courseId: String) = launchWithLoading {
        when (val result = getACourseDetailUseCase(courseId)) {
            is Resource.Success -> {
                val course = result.data
                setState {
                    copy(
                        courseDetail = courseDetail.copy(
                            titulo = course.titulo,
                            descripcion = course.descripcion,
                            horaIncio = course.horaIncio,
                            horaFin = course.horaFin,
                            fecha = course.fecha,
                            costo = course.costo,
                            apartado = course.apartado,
                            instructora = course.instructora,
                            instructoraDesc = course.instructoraDesc,
                            temarios = course.temarios,
                            imagen = course.imagen,
                            instructoraImage = course.instructoraImage,
                            ubicacionNombre = course.ubicacionNombre,
                            lat = course.lat,
                            lng = course.lng,
                            banner = course.banner,
                            id = course.id
                        )
                    )
                }
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