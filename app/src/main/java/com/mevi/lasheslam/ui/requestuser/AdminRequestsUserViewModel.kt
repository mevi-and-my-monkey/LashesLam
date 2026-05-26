package com.mevi.lasheslam.ui.requestuser

import androidx.lifecycle.viewModelScope
import com.mevi.lasheslam.BaseViewModel
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.analytics.AnalyticsEvent
import com.mevi.lasheslam.domain.model.SessionData
import com.mevi.lasheslam.domain.repository.AnalyticsTracker
import com.mevi.lasheslam.domain.usecase.GetAllRequestsUseCase
import com.mevi.lasheslam.domain.usecase.GetCurrentUserIdUseCase
import com.mevi.lasheslam.domain.usecase.GetIsAdminUseCase
import com.mevi.lasheslam.domain.usecase.GetIsUserInvitedUseCase
import com.mevi.lasheslam.domain.usecase.GetNameUserUseCase
import com.mevi.lasheslam.domain.usecase.GetPhotoUserUseCase
import com.mevi.lasheslam.ui.home.components.Section
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminRequestsUserViewModel @Inject constructor(
    private val analytics: AnalyticsTracker,
    private val getIsAdminUseCase: GetIsAdminUseCase,
    private val getIsUserInvitedUseCase: GetIsUserInvitedUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val getNameUserUseCase: GetNameUserUseCase,
    private val getPhotoUserUseCase: GetPhotoUserUseCase,
    private val getAllRequestsUseCase: GetAllRequestsUseCase,
    ) : BaseViewModel<RequestUserUiState, RequestUserUiEvent>() {

    override fun createInitialState() = RequestUserUiState()

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
                    loadRequests(userId)
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
            }

            Section.SERVICIOS -> {
                trackEvent(AnalyticsEvent.SectionSelected(section.name))
                setState { copy(selectedSection = section) }
            }
        }
    }

    fun loadRequests(userId : String) = launchWithLoading {
        when (val result = getAllRequestsUseCase(userId)) {
            is Resource.Success -> {
                setState { copy(requestUserCourses = result.data) }

            }
            is Resource.Error -> {
                sendEvent(RequestUserUiEvent.ShowError(result.error))
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
