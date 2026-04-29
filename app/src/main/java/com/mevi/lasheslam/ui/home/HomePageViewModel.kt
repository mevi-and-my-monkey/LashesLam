package com.mevi.lasheslam.ui.home

import androidx.lifecycle.viewModelScope
import com.mevi.lasheslam.BaseViewModel
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.data.constants.FirestorePaths
import com.mevi.lasheslam.domain.analytics.AnalyticsEvent
import com.mevi.lasheslam.domain.analytics.AuthMethod
import com.mevi.lasheslam.domain.analytics.toAnalyticsType
import com.mevi.lasheslam.domain.usecase.GetAllCoursesUseCase
import com.mevi.lasheslam.domain.usecase.GetCoursesUseCase
import com.mevi.lasheslam.domain.usecase.GetCurrentUserIdUseCase
import com.mevi.lasheslam.domain.usecase.GetIsAdminUseCase
import com.mevi.lasheslam.domain.usecase.GetIsUserInvitedUseCase
import com.mevi.lasheslam.domain.usecase.GetRequestsUseCase
import com.mevi.lasheslam.ui.auth.LoginUiEvent
import com.mevi.lasheslam.ui.home.components.Section
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomePageViewModel @Inject constructor(
    private val getACoursesUseCase: GetCoursesUseCase,
    private val getIsAdminUseCase: GetIsAdminUseCase,
    private val getIsUserInvitedUseCase: GetIsUserInvitedUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val getRequestsUseCase: GetRequestsUseCase,
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

            getACoursesUseCase().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        setState {
                            copy(
                                courses = result.data.sortedByDescending { it.fecha },
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
                Triple(isAdmin, isInvited, userId)
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
                } else if (userId != null) {
                    //loadUserAcceptedCourses(userId)
                }
            }
        }
    }

    fun onSectionSelected(section: Section) {
        when (section) {
            Section.CURSOS -> {
                setState { copy(selectedSection = section) }
            }

            else -> {
                viewModelScope.launch {
                    sendEvent(HomeUiEvent.ShowComingSoon)
                }
            }
        }
    }
}