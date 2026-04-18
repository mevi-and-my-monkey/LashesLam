package com.mevi.lasheslam.ui.auth

import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.mevi.lasheslam.BaseViewModel
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.usecase.LoginUseCase
import com.mevi.lasheslam.domain.usecase.RegisterUseCase
import com.mevi.lasheslam.domain.usecase.SaveSessionUseCase
import com.mevi.lasheslam.domain.usecase.SignInWithGoogleUseCase
import com.mevi.lasheslam.network.UserModel
import com.mevi.lasheslam.ui.common.toUserMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val googleUseCase: SignInWithGoogleUseCase,
    private val saveSessionUseCase: SaveSessionUseCase
) : BaseViewModel<LoginUiState, LoginUiEvent>() {

    override fun createInitialState() = LoginUiState()

    fun login() {
        viewModelScope.launch {
            setState { copy(isLoading = true) }

            when (val result = loginUseCase(uiState.value.email, uiState.value.password)) {
                is Resource.Success -> {
                    saveSessionUseCase(uiState.value.email)
                    setState { copy(isLoading = false) }
                    sendEvent(LoginUiEvent.NavigateToHome)
                }

                is Resource.Error -> {
                    setState { copy(isLoading = false) }
                    val message = result.error.toUserMessage()
                    sendEvent(LoginUiEvent.ShowError(message))
                }

                else -> {}
            }
        }
    }

    fun register(user: UserModel) {
        viewModelScope.launch {
            setState { copy(isLoading = true) }

            when (val result = registerUseCase(user)) {
                is Resource.Success -> {
                    user.email?.let {
                        saveSessionUseCase(it)
                    }
                    setState { copy(isLoading = false) }
                    sendEvent(LoginUiEvent.RegisterSuccess)
                }

                is Resource.Error -> {
                    setState { copy(isLoading = false) }
                    val message = result.error.toUserMessage()
                    sendEvent(LoginUiEvent.ShowError(message))
                }

                else -> {}
            }
        }
    }

    fun signInWithGoogle(credential: AuthCredential, email: String?) {
        viewModelScope.launch {
            setState { copy(isLoading = true) }

            when (val result = googleUseCase(credential)) {
                is Resource.Success -> {
                    email?.let {
                        saveSessionUseCase(it)
                    }
                    setState { copy(isLoading = false) }
                    sendEvent(LoginUiEvent.NavigateToHome)
                }

                is Resource.Error -> {
                    setState { copy(isLoading = false) }
                    val message = result.error.toUserMessage()
                    sendEvent(LoginUiEvent.ShowError(message))
                }

                else -> {}
            }
        }
    }

    fun onLoginChanged(email: String, password: String) {
        setState {
            copy(
                email = email,
                password = password,
                isLoginEnabled = enableLogin(email, password)
            )
        }
    }

    private fun enableLogin(email: String, password: String) =
        Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$").matches(email) &&
                password.length > 6
}