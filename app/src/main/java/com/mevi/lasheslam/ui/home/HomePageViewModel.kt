package com.mevi.lasheslam.ui.home

import androidx.lifecycle.viewModelScope
import com.mevi.lasheslam.BaseViewModel
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.usecase.GetAllCoursesUseCase
import com.mevi.lasheslam.domain.usecase.GetIsAdminUseCase
import com.mevi.lasheslam.ui.home.components.Section
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomePageViewModel @Inject constructor(
    private val getAllCoursesUseCase: GetAllCoursesUseCase,
    private val getIsAdminUseCase: GetIsAdminUseCase
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

            getAllCoursesUseCase().collect { result ->
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

    private fun observeSession() {
        viewModelScope.launch {
            getIsAdminUseCase().collect { isAdmin ->
                setState { copy(isAdmin = isAdmin) }
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