package com.mevi.lasheslam.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mevi.lasheslam.session.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
) : ViewModel() {

    var name by mutableStateOf("")
        private set

    var photoUrl by mutableStateOf<String?>(null)
        private set

    val isUserInvited = SessionManager.isUserInvited

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _selectedService = MutableStateFlow<Map<String, Any>?>(null)
    val selectedService: StateFlow<Map<String, Any>?> = _selectedService

    fun loadServiceById(serviceId: String) {
        showLoading()
        FirebaseFirestore.getInstance()
            .collection("data")
            .document("curse")
            .collection("items")
            .document(serviceId)
            .addSnapshotListener { snapshot, e ->
                if (snapshot != null && snapshot.exists()) {
                    _selectedService.value = snapshot.data
                } else {
                    _selectedService.value = null
                }
            }
        hideLoading()
    }

    init {
        loadUserData()
    }

    fun showLoading() {
        _isLoading.value = true
    }

    fun hideLoading() {
        _isLoading.value = false
    }

    private fun loadUserData() {
        val user = auth.currentUser ?: return
        photoUrl = user.photoUrl?.toString()

        viewModelScope.launch {
            if (isUserInvited.value) {
                name = "Invitado"
            } else {
                firestore.collection("users").document(user.uid)
                    .get()
                    .addOnSuccessListener {
                        name = it.getString("name")?.split(" ")?.firstOrNull() ?: ""
                    }
            }
        }
    }
}