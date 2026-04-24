package com.mevi.lasheslam.ui.auth

import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.mevi.lasheslam.BaseViewModel
import com.mevi.lasheslam.core.error.ErrorMapper
import com.mevi.lasheslam.domain.analytics.AnalyticsEvent
import com.mevi.lasheslam.domain.analytics.AuthMethod
import com.mevi.lasheslam.domain.analytics.toAnalyticsType
import com.mevi.lasheslam.domain.repository.AnalyticsTracker
import com.mevi.lasheslam.domain.usecase.LoginUseCase
import com.mevi.lasheslam.domain.usecase.RegisterUseCase
import com.mevi.lasheslam.domain.usecase.SaveSessionUseCase
import com.mevi.lasheslam.domain.usecase.SignInWithGoogleUseCase
import com.mevi.lasheslam.domain.usecase.ValidateLoginUseCase
import com.mevi.lasheslam.network.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val googleUseCase: SignInWithGoogleUseCase,
    private val saveSessionUseCase: SaveSessionUseCase,
    private val errorMapper: ErrorMapper,
    private val validateLoginUseCase: ValidateLoginUseCase,
    private val analytics: AnalyticsTracker
) : BaseViewModel<LoginUiState, LoginUiEvent>() {

    override fun createInitialState() = LoginUiState()

    fun onError(e: Exception) {
        viewModelScope.launch {
            val error = errorMapper.map(e)
            trackEvent(AnalyticsEvent.LoginError(error.toAnalyticsType()))
            sendError(error) { LoginUiEvent.ShowError(it) }
        }
    }

    fun login() = launchWithLoading {
        val result = loginUseCase(uiState.value.email, uiState.value.password)
        handleResult(
            result = result,
            onSuccess = {
                trackEvent(AnalyticsEvent.LoginSuccess(AuthMethod.EMAIL))
                saveSessionUseCase(uiState.value.email)
                sendEvent(LoginUiEvent.NavigateToHome)
            },
            onError = { error ->
                trackEvent(AnalyticsEvent.LoginError(error.toAnalyticsType()))
                sendError(error) { LoginUiEvent.ShowError(it) }
            }
        )
    }

    fun register(user: UserModel) = launchWithLoading {
        val result = registerUseCase(user)

        handleResult(
            result = result,
            onSuccess = {
                trackEvent(AnalyticsEvent.RegisterSuccess(AuthMethod.EMAIL))
                user.email?.let {
                    saveSessionUseCase(it)
                }
                sendEvent(LoginUiEvent.RegisterSuccess)
            },
            onError = { error ->
                trackEvent(AnalyticsEvent.RegisterError(error.toAnalyticsType()))
                sendError(error) { LoginUiEvent.ShowError(it) }
            })
    }

    fun signInWithGoogle(credential: AuthCredential, email: String?) = launchWithLoading {
        val result = googleUseCase(credential)
        handleResult(
            result = result,
            onSuccess = {
                trackEvent(AnalyticsEvent.LoginSuccess(AuthMethod.GOOGLE))
                email?.let {
                    saveSessionUseCase(it)
                }
                sendEvent(LoginUiEvent.NavigateToHome)
            },
            onError = { error ->
                trackEvent(AnalyticsEvent.LoginError(error.toAnalyticsType()))
                sendError(error) { LoginUiEvent.ShowError(it) }
            })
    }

    fun onLoginChanged(email: String, password: String) {
        val isValid = validateLoginUseCase(email, password)
        setState {
            copy(
                email = email,
                password = password,
                isLoginEnabled = isValid
            )
        }
    }

    fun trackEvent(event: AnalyticsEvent) {
        analytics.track(event)
    }

    fun trackScreen(screen: String) {
        analytics.track(AnalyticsEvent.ScreenView(screen))
    }
}