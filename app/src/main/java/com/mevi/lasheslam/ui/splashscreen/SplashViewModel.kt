package com.mevi.lasheslam.ui.splashscreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mevi.lasheslam.domain.usecase.CheckUpdateUseCase
import com.mevi.lasheslam.core.results.UpdateResult
import com.mevi.lasheslam.domain.usecase.GetSessionUseCase
import com.mevi.lasheslam.domain.usecase.RefreshSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val checkUpdateUseCase: CheckUpdateUseCase,
    private val getSessionUseCase: GetSessionUseCase,
    private val refreshSessionUseCase: RefreshSessionUseCase
) : ViewModel() {

    var state by mutableStateOf<SplashState>(SplashState.Loading)
        private set


    fun init() {
        viewModelScope.launch {

            // Update
            when (val update = checkUpdateUseCase()) {
                is UpdateResult.Required -> {
                    state = SplashState.ForceUpdate(update.appUpdateInfo)
                    return@launch
                }
                else -> {}
            }

            // Session
            refreshSessionUseCase()

            val isLoggedIn = getSessionUseCase()

            state = if (isLoggedIn) {
                SplashState.GoToHome
            } else {
                SplashState.GoToLogin
            }
        }
    }
}