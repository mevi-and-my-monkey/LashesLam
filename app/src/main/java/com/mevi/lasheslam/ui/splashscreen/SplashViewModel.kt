package com.mevi.lasheslam.ui.splashscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mevi.lasheslam.core.results.UpdateResult
import com.mevi.lasheslam.domain.usecase.CheckUpdateUseCase
import com.mevi.lasheslam.domain.usecase.GetSessionUseCase
import com.mevi.lasheslam.domain.usecase.RefreshSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val checkUpdateUseCase: CheckUpdateUseCase,
    private val getSessionUseCase: GetSessionUseCase,
    private val refreshSessionUseCase: RefreshSessionUseCase
) : ViewModel() {

    private val _effect = MutableSharedFlow<SplashEffect>(replay = 1)
    val effect = _effect

    private var initialized = false

    fun init() {
        if (initialized) return
        initialized = true

        viewModelScope.launch {

            // Update
            when (checkUpdateUseCase()) {
                UpdateResult.Required -> {
                    _effect.emit(SplashEffect.ForceUpdate)
                    return@launch
                }

                UpdateResult.NotRequired -> Unit
            }
            // Session
            refreshSessionUseCase()

            val isLoggedIn = getSessionUseCase()

            if (isLoggedIn) {
                _effect.emit(SplashEffect.NavigateHome)
            } else {
                _effect.emit(SplashEffect.NavigateLogin)
            }
        }
    }
}