package com.mevi.lasheslam.ui.auth

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.compose.runtime.*
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.mevi.lasheslam.core.AuthState
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.usecase.LoginUseCase
import com.mevi.lasheslam.domain.usecase.RegisterUseCase
import com.mevi.lasheslam.domain.usecase.SignInWithGoogleUseCase
import com.mevi.lasheslam.network.UserModel
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val googleUseCase: SignInWithGoogleUseCase
) : ViewModel() {


    var isLoading by mutableStateOf(false)
        private set

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var isLoginEnable by mutableStateOf(false)
        private set

    var authState by mutableStateOf<AuthState>(AuthState.Idle)
        private set

    private val _adminEmails = mutableStateOf(listOf(""))
    val adminEmails: State<List<String>> = _adminEmails

    fun setAdminEmails(emails: List<String>?) {
        _adminEmails.value = emails ?: listOf("")
    }

    fun showLoading() {
        isLoading = true
    }

    fun hideLoading() {
        isLoading = false
    }


    fun login(onResult: (Boolean, String?) -> Unit) = viewModelScope.launch {
        isLoading = true
        when (val result = loginUseCase(email, password)) {
            is Resource.Success -> {
                authState = AuthState.Success
                isLoading = false
                onResult(true, null)
            }

            is Resource.Error -> {
                authState = AuthState.Error(result.message)
                isLoading = false
                onResult(false, result.message)
            }

            else -> Unit
        }
    }

    fun register(user: UserModel, onResult: (Boolean, String?) -> Unit) = viewModelScope.launch {
        isLoading = true
        when (val result = registerUseCase(user)) {
            is Resource.Success -> {
                authState = AuthState.Success
                isLoading = false
                onResult(true, null)
            }

            is Resource.Error -> {
                authState = AuthState.Error(result.message)
                isLoading = false
                onResult(false, result.message)
            }

            else -> Unit
        }
    }

    fun signInWithGoogle(credential: AuthCredential, onResult: (Boolean, String?) -> Unit) =
        viewModelScope.launch {
            isLoading = true
            when (val result = googleUseCase(credential)) {
                is Resource.Success -> {
                    authState = AuthState.Success
                    isLoading = false
                    onResult(true, null)
                }

                is Resource.Error -> {
                    authState = AuthState.Error(result.message)
                    isLoading = false
                    onResult(false, result.message)
                }

                else -> Unit
            }
        }

    fun onLoginChanged(email: String, password: String) {
        this.email = email
        this.password = password
        isLoginEnable = enableLogin(email = email, password = password)
    }

    private fun enableLogin(email: String, password: String) =
        Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$").matches(email) && password.length > 6

}