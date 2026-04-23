package com.mevi.lasheslam.ui.auth

import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.mevi.lasheslam.BaseViewModel
import com.mevi.lasheslam.core.error.ErrorMapper
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
    private val validateLoginUseCase: ValidateLoginUseCase
) : BaseViewModel<LoginUiState, LoginUiEvent>() {

    override fun createInitialState() = LoginUiState()

    fun onError(e: Exception) {
        viewModelScope.launch {
            sendError(errorMapper.map(e)){ LoginUiEvent.ShowError(it) }
        }
    }

    fun login() = launchWithLoading {
        val result = loginUseCase(uiState.value.email, uiState.value.password)
        handleResult(
            result = result,
            onSuccess = {
                saveSessionUseCase(uiState.value.email)
                sendEvent(LoginUiEvent.NavigateToHome)
            },
            onError = { error ->
                sendError(error) { LoginUiEvent.ShowError(it) }
            }
        )
    }

    fun register(user: UserModel) = launchWithLoading {

        val result = registerUseCase(user)

        handleResult(
            result = result,
            onSuccess = {
                user.email?.let {
                    saveSessionUseCase(it)
                }
                sendEvent(LoginUiEvent.RegisterSuccess)
            },
            onError = { error ->
                sendError(error) { LoginUiEvent.ShowError(it) }
            })
    }

    fun signInWithGoogle(credential: AuthCredential, email: String?) = launchWithLoading {
        val result = googleUseCase(credential)
        handleResult(
            result = result,
            onSuccess = {
                email?.let {
                    saveSessionUseCase(it)
                }
                sendEvent(LoginUiEvent.NavigateToHome)
            },
            onError = { error ->
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

}