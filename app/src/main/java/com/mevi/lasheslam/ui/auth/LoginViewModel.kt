package com.mevi.lasheslam.ui.auth

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.compose.runtime.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.mevi.lasheslam.core.AuthState
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.usecase.GetIsDarkModeUseCase
import com.mevi.lasheslam.domain.usecase.LoginUseCase
import com.mevi.lasheslam.domain.usecase.RegisterUseCase
import com.mevi.lasheslam.domain.usecase.SaveIsDarkModeUseCase
import com.mevi.lasheslam.domain.usecase.SignInWithGoogleUseCase
import com.mevi.lasheslam.network.UserModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val googleUseCase: SignInWithGoogleUseCase,
    private val saveIsDarkModeUseCase: SaveIsDarkModeUseCase,
    private val getUserSessionUseCase: GetIsDarkModeUseCase
) : ViewModel() {


    val isDarkMode: StateFlow<Boolean> = getUserSessionUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var isLoginEnable by mutableStateOf(false)
        private set

    var authState by mutableStateOf<AuthState>(AuthState.Idle)
        private set

    fun showLoading() {
        _isLoading.value = true
    }

    fun hideLoading() {
        _isLoading.value = false
    }


    fun login(onResult: (Boolean, String?) -> Unit) = viewModelScope.launch {
        showLoading()
        when (val result = loginUseCase(email, password)) {
            is Resource.Success -> {
                authState = AuthState.Success
                onResult(true, null)
            }

            is Resource.Error -> {
                authState = AuthState.Error(result.message)
                onResult(false, result.message)
            }

            else -> Unit
        }
        hideLoading()
    }

    fun register(user: UserModel, onResult: (Boolean, String?) -> Unit) = viewModelScope.launch {
        showLoading()
        when (val result = registerUseCase(user)) {
            is Resource.Success -> {
                authState = AuthState.Success
                onResult(true, null)
            }

            is Resource.Error -> {
                authState = AuthState.Error(result.message)
                onResult(false, result.message)
            }

            else -> Unit
        }
        hideLoading()
    }

    fun signInWithGoogle(credential: AuthCredential, onResult: (Boolean, String?) -> Unit) =
        viewModelScope.launch {
            showLoading()
            when (val result = googleUseCase(credential)) {
                is Resource.Success -> {
                    authState = AuthState.Success
                    onResult(true, null)
                }

                is Resource.Error -> {
                    authState = AuthState.Error(result.message)
                    onResult(false, result.message)
                }

                else -> Unit
            }
            hideLoading()
        }

    fun toggleDarkMode(enabled: Boolean) = viewModelScope.launch {
        saveIsDarkModeUseCase(enabled)
    }

    fun onLoginChanged(email: String, password: String) {
        this.email = email
        this.password = password
        isLoginEnable = enableLogin(email = email, password = password)
    }

    private fun enableLogin(email: String, password: String) =
        Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$").matches(email) && password.length > 6

}