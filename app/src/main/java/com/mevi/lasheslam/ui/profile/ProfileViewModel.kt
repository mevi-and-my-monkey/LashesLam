package com.mevi.lasheslam.ui.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mevi.lasheslam.data.DataStoreRepository
import com.mevi.lasheslam.navigation.Screen
import com.mevi.lasheslam.network.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    var userModel by mutableStateOf(UserModel())
        private set

    var isLoading by mutableStateOf(false)
        private set

    val isDarkMode = dataStoreRepository.darkMode

    var photoUser by mutableStateOf("")
        private set

    fun toggleDarkMode(enabled: Boolean) = viewModelScope.launch {
        dataStoreRepository.setDarkMode(enabled)
    }

    fun loadUserData() {
        val user = auth.currentUser ?: return
        photoUser = user.photoUrl?.toString() ?: ""
        firestore.collection("users").document(user.uid)
            .get()
            .addOnSuccessListener { snapshot ->
                snapshot.toObject(UserModel::class.java)?.let {
                    userModel = it
                }
            }
    }

    fun updateAddress(newAddress: String,onResult: (Boolean, String?) -> Unit) {
        val uid = auth.currentUser?.uid ?: return
        if (newAddress.isBlank()) {
            onResult(false,"La dirección no puede estar vacía")
            return
        }
        isLoading = true
        firestore.collection("users").document(uid)
            .update("address", newAddress)
            .addOnSuccessListener {
                isLoading = false
                onResult(true, null)
            }
            .addOnFailureListener {
                isLoading = false
                onResult(false,"Error al actualizar dirección")
            }
    }

    fun updatePhone(newPhone: String, onResult: (Boolean, String?) -> Unit) {
        val uid = auth.currentUser?.uid ?: return
        isLoading = true
        firestore.collection("users").document(uid)
            .update("phone", newPhone)
            .addOnSuccessListener {
                isLoading = false
                onResult(true, null)
            }
            .addOnFailureListener {
                isLoading = false
                onResult(false, "Error al actualizar el numero telefonico")
            }
    }

    fun signOut(navController: NavController) {
        auth.signOut()
        navController.navigate(Screen.Login.route) {
            popUpTo(0) { inclusive = true }
            launchSingleTop = true
        }
    }
}