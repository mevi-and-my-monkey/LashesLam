package com.mevi.lasheslam.ui.splashscreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mevi.lasheslam.domain.usecase.CheckUpdateUseCase
import com.mevi.lasheslam.core.results.UpdateResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val checkUpdateUseCase: CheckUpdateUseCase
) : ViewModel() {

    var updateState by mutableStateOf<UpdateResult?>(null)
        private set

    fun checkUpdate() {
        viewModelScope.launch {
            updateState = checkUpdateUseCase()
        }
    }
}