package com.mevi.lasheslam

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mevi.lasheslam.domain.usecase.GetDarkModeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(getDarkModeUseCase: GetDarkModeUseCase) : ViewModel() {
    val isDarkMode = getDarkModeUseCase()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            false
        )
}