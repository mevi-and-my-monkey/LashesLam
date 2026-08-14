package com.mevi.lasheslam.ui.profile

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.repository.SessionDataSource
import com.mevi.lasheslam.domain.repository.UserPreferencesRepository
import com.mevi.lasheslam.domain.usecase.GetUserProfileUseCase
import com.mevi.lasheslam.domain.usecase.SignOutUseCase
import com.mevi.lasheslam.domain.usecase.UpdateAddressUseCase
import com.mevi.lasheslam.domain.usecase.UpdatePhoneUseCase
import com.mevi.lasheslam.domain.usecase.UpdateUserPhotoUseCase
import com.mevi.lasheslam.domain.usecase.cart.ClearCartUseCase
import com.mevi.lasheslam.domain.model.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val updateAddressUseCase: UpdateAddressUseCase,
    private val updatePhoneUseCase: UpdatePhoneUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val updateUserPhotoUseCase: UpdateUserPhotoUseCase,
    private val clearCartUseCase: ClearCartUseCase,
    private val sessionDataSource: SessionDataSource
) : ViewModel() {

    var userModel by mutableStateOf(UserModel())
        private set

    var isLoading by mutableStateOf(false)
        private set

    val isDarkMode = userPreferencesRepository.darkMode

    var photoUser by mutableStateOf("")
        private set

    fun toggleDarkMode(enabled: Boolean) = viewModelScope.launch {
        userPreferencesRepository.setDarkMode(enabled)
    }

    fun loadUserData() {
        viewModelScope.launch {
            when (val result = getUserProfileUseCase()) {
                is Resource.Success -> {
                    userModel = result.data
                    photoUser = result.data.userPhoto.orEmpty()
                }

                is Resource.Error -> {}
            }
        }
    }

    fun updateProfilePhoto(imageUri: Uri, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            isLoading = true
            when (val result = updateUserPhotoUseCase(imageUri)) {
                is Resource.Success -> {
                    photoUser = result.data
                    userModel = userModel.copy(
                        userPhoto = result.data,
                        photoUpdatedByUser = true
                    )
                    sessionDataSource.setPhotoUrl(result.data)
                    isLoading = false
                    onResult(true, null)
                }

                is Resource.Error -> {
                    isLoading = false
                    onResult(false, "Error al actualizar la foto de perfil")
                }
            }
        }
    }

    fun updateAddress(newAddress: String, onResult: (Boolean, String?) -> Unit) {
        if (newAddress.isBlank()) {
            onResult(false, "La dirección no puede estar vacía")
            return
        }
        viewModelScope.launch {
            isLoading = true
            val result = updateAddressUseCase(newAddress)
            isLoading = false
            when (result) {
                is Resource.Success -> onResult(true, null)
                is Resource.Error -> onResult(false, "Error al actualizar dirección")
            }
        }
    }

    fun updatePhone(newPhone: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            isLoading = true
            val result = updatePhoneUseCase(newPhone)
            isLoading = false
            when (result) {
                is Resource.Success -> onResult(true, null)
                is Resource.Error -> onResult(false, "Error al actualizar el numero telefonico")
            }
        }
    }

    fun signOut(onNavigateToLogOut: () -> Unit) {
        clearCartUseCase()
        sessionDataSource.clearUserSession()
        signOutUseCase()
        onNavigateToLogOut()
    }
}
