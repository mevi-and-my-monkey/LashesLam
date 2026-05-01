package com.mevi.lasheslam.ui.home

import androidx.lifecycle.viewModelScope
import com.mevi.lasheslam.BaseViewModel
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.data.constants.FirestorePaths
import com.mevi.lasheslam.domain.analytics.AnalyticsEvent
import com.mevi.lasheslam.domain.model.SessionData
import com.mevi.lasheslam.domain.notifications.ObserveUserCoursesUseCase
import com.mevi.lasheslam.domain.repository.AnalyticsTracker
import com.mevi.lasheslam.domain.usecase.GetCoursesUseCase
import com.mevi.lasheslam.domain.usecase.GetCurrentUserIdUseCase
import com.mevi.lasheslam.domain.usecase.GetIsAdminUseCase
import com.mevi.lasheslam.domain.usecase.GetIsUserInvitedUseCase
import com.mevi.lasheslam.domain.usecase.GetNameUserUseCase
import com.mevi.lasheslam.domain.usecase.GetPhotoUserUseCase
import com.mevi.lasheslam.domain.usecase.GetRequestsUseCase
import com.mevi.lasheslam.domain.usecase.HandleCourseNotificationsUseCase
import com.mevi.lasheslam.ui.home.components.Section
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class HomePageViewModel @Inject constructor(
    private val analytics: AnalyticsTracker,
    private val getACoursesUseCase: GetCoursesUseCase,
    private val getIsAdminUseCase: GetIsAdminUseCase,
    private val getIsUserInvitedUseCase: GetIsUserInvitedUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val getRequestsUseCase: GetRequestsUseCase,
    private val observeUserCoursesUseCase: ObserveUserCoursesUseCase,
    private val handleCourseNotificationsUseCase: HandleCourseNotificationsUseCase,
    private val getNameUserUseCase: GetNameUserUseCase,
    private val getPhotoUserUseCase: GetPhotoUserUseCase
) : BaseViewModel<HomePageUiState, HomeUiEvent>() {

    override fun createInitialState() = HomePageUiState()

    init {
        loadCourses()
        observeSession()
    }

    private var isCoursesLoaded = false

    fun loadCourses() {
        if (isCoursesLoaded) return
        isCoursesLoaded = true

        viewModelScope.launch {
            setState { copy(isLoading = true) }

            val today = Calendar.getInstance().time

            getACoursesUseCase().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        val filteredCourses = result.data
                            .filter { it.date >= today }
                            .sortedBy { it.date }
                        setState {
                            copy(
                                courses = filteredCourses,
                                isLoading = false
                            )
                        }
                    }

                    is Resource.Error -> {
                        setState { copy(isLoading = false) }
                        sendError(result.error) { HomeUiEvent.ShowError(it) }
                    }
                }
            }
        }
    }

    fun loadAdminPendingRequests() {
        viewModelScope.launch {
            val result = getRequestsUseCase(FirestorePaths.Courses.STATUS_PANDING)

            when (result) {
                is Resource.Success -> {
                    setState { copy(adminPendingCount = result.data.size) }
                }

                is Resource.Error -> {
                    setState { copy(adminPendingCount = 0) }
                    sendError(result.error) { HomeUiEvent.ShowError(it) }
                }
            }
        }
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
                if (isAdmin) {
                    loadAdminPendingRequests()
                } else if (userId.isNotEmpty()) {
                    observeUserCourses(userId)
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
                        isProfileLoading = false
                    )
                }
            }
        }
    }

    private var observeJob: Job? = null

    fun observeUserCourses(userId: String) {
        if (observeJob?.isActive == true) return
        observeJob = viewModelScope.launch {
            observeUserCoursesUseCase(userId).collect { courses ->
                handleCourseNotificationsUseCase(userId, courses)
            }
        }
    }

    fun onSectionSelected(section: Section) {
        when (section) {
            Section.CURSOS -> {
                trackEvent(AnalyticsEvent.SectionSelected(section.name))
                setState { copy(selectedSection = section) }
            }

            else -> {
                viewModelScope.launch {
                    sendEvent(HomeUiEvent.ShowComingSoon)
                }
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