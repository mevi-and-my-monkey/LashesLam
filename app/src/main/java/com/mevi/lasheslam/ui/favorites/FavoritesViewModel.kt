package com.mevi.lasheslam.ui.favorites

import androidx.lifecycle.viewModelScope
import com.mevi.lasheslam.BaseViewModel
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.analytics.AnalyticsEvent
import com.mevi.lasheslam.domain.model.SessionData
import com.mevi.lasheslam.domain.repository.AnalyticsTracker
import com.mevi.lasheslam.domain.usecase.GetCurrentUserIdUseCase
import com.mevi.lasheslam.domain.usecase.GetFavoriteCoursesUseCase
import com.mevi.lasheslam.domain.usecase.GetFavoritesUseCase
import com.mevi.lasheslam.domain.usecase.GetIsAdminUseCase
import com.mevi.lasheslam.domain.usecase.GetIsUserInvitedUseCase
import com.mevi.lasheslam.domain.usecase.GetNameUserUseCase
import com.mevi.lasheslam.domain.usecase.GetPhotoUserUseCase
import com.mevi.lasheslam.domain.usecase.products.GetFavoriteProductsUseCase
import com.mevi.lasheslam.domain.usecase.service.GetFavoriteServicesUseCase
import com.mevi.lasheslam.ui.home.components.Section
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val analytics: AnalyticsTracker,
    private val getIsAdminUseCase: GetIsAdminUseCase,
    private val getIsUserInvitedUseCase: GetIsUserInvitedUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val getNameUserUseCase: GetNameUserUseCase,
    private val getPhotoUserUseCase: GetPhotoUserUseCase,
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val getFavoriteCoursesUseCase: GetFavoriteCoursesUseCase,
    private val getFavoriteProductsUseCase: GetFavoriteProductsUseCase,
    private val getFavoriteServicesUseCase: GetFavoriteServicesUseCase,

    ) : BaseViewModel<FavoritesUiState, FavoriteUiEvent>() {

    override fun createInitialState() = FavoritesUiState()

    init {
        observeSession()
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
                    loadFavoriteCourses()
                }
            }
        }
        viewModelScope.launch {
            combine(
                getNameUserUseCase(),
                getPhotoUserUseCase()
            ) { name, photo ->
                name to photo
            }.collect { (name, photo) ->
                setState {
                    copy(
                        nameUser = name,
                        photoUser = photo,
                    )
                }
            }
        }
    }

    fun onSectionSelected(section: Section) {
        when (section) {
            Section.CURSOS -> {
                trackEvent(AnalyticsEvent.SectionSelected(section.name))
                setState { copy(selectedSection = section) }
            }

            Section.PRODUCTOS -> {
                trackEvent(AnalyticsEvent.SectionSelected(section.name))
                setState { copy(selectedSection = section) }
                loadFavoriteProducts()
            }

            Section.SERVICIOS -> {
                trackEvent(AnalyticsEvent.SectionSelected(section.name))
                setState { copy(selectedSection = section) }
                loadFavoriteServices()
            }
        }
    }

    fun loadFavoriteCourses() = launchWithLoading {
        when (val favResult = getFavoritesUseCase(uiState.value.currentUserId ?: "")) {
            is Resource.Success -> {

                val ids = favResult.data
                    .filter { it.type == FavoriteType.COURSE.name || it.type == null }
                    .map { it.itemId }

                if (ids.isEmpty()) {
                    setState { copy(favoriteCourses = emptyList()) }
                } else {
                    when (val courseResult = getFavoriteCoursesUseCase(ids)) {
                        is Resource.Success -> {
                            setState { copy(favoriteCourses = courseResult.data) }
                        }

                        else -> {}
                    }
                }
            }

            is Resource.Error -> {
                sendEvent(FavoriteUiEvent.ShowError(favResult.error))
            }
        }
    }

    fun loadFavoriteProducts() = launchWithLoading {
        when (val result = getFavoritesUseCase(uiState.value.currentUserId ?: "")) {
            is Resource.Success -> {

                val ids = result.data
                    .filter { it.type == FavoriteType.PRODUCT.name || it.type == null }
                    .map { it.itemId }

                if (ids.isEmpty()) {
                    setState { copy(favoriteProducts = emptyList()) }
                } else {
                    when (val productResult = getFavoriteProductsUseCase(ids)) {
                        is Resource.Success -> {
                            setState { copy(favoriteProducts = productResult.data) }
                        }

                        else -> {}
                    }
                }
            }

            is Resource.Error -> {
                sendEvent(FavoriteUiEvent.ShowError(result.error))
            }
        }
    }

    fun loadFavoriteServices() = launchWithLoading {
        when (val result = getFavoritesUseCase(uiState.value.currentUserId ?: "")) {
            is Resource.Success -> {

                val ids = result.data
                    .filter { it.type == FavoriteType.SERVICE.name || it.type == null }
                    .map { it.itemId }

                if (ids.isEmpty()) {
                    setState { copy(favoriteServices = emptyList()) }
                } else {
                    when (val serviceResult = getFavoriteServicesUseCase(ids)) {
                        is Resource.Success -> {
                            setState { copy(favoriteServices = serviceResult.data) }
                        }

                        else -> {}
                    }
                }
            }

            is Resource.Error -> {
                sendEvent(FavoriteUiEvent.ShowError(result.error))
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