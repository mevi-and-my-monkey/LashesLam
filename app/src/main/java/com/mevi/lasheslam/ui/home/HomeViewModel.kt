package com.mevi.lasheslam.ui.home

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.usecase.GetFavoritesUseCase
import com.mevi.lasheslam.domain.usecase.GetRequestsUseCase
import com.mevi.lasheslam.domain.usecase.ToggleFavoriteUseCase
import com.mevi.lasheslam.session.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val getRequestsUseCase: GetRequestsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val getFavoritesUseCase: GetFavoritesUseCase,
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

    private val _courseStatusCurse = MutableStateFlow<String?>(null)
    val courseStatusCurse: StateFlow<String?> = _courseStatusCurse

    var adminPendingCount by mutableStateOf(0)
        private set

    var userAcceptedCount by mutableStateOf(0)
        private set

    private val _isFavorite = mutableStateOf(false)
    val isFavorite: State<Boolean> get() = _isFavorite

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
                        SessionManager.setNameUser(name)
                    }
            }
        }
    }

    suspend fun createCourseRequest(
        userId: String,
        courseId: String,
        courseName: String,
        date: String,
        schedule: String,
        nameUser: String,
        emailUser: String
    ) {
        try {
            val requestRef = firestore.collection("course_requests").document()
            val requestId = requestRef.id

            val requestData = mapOf(
                "requestId" to requestId,
                "userId" to userId,
                "nameUser" to nameUser,
                "emailUser" to emailUser,
                "courseId" to courseId,
                "courseName" to courseName,
                "status" to "pendiente",
                "date" to date,
                "schedule" to schedule,
                "timestamp" to System.currentTimeMillis()
            )

            println("DEBUG: Intentando crear solicitud $requestId")

            requestRef.set(requestData).await()

            println("DEBUG: Solicitud creada en course_requests")

            firestore.collection("users")
                .document(userId)
                .collection("cursos")
                .document(courseId)
                .set(mapOf("status" to "pendiente"))
                .await()

        } catch (e: Exception) {
            e.printStackTrace()
            println("ERROR creando solicitud: ${e.message}")
        }
    }

    fun loadUserCourseStatus(userId: String, courseId: String) {
        firestore.collection("users")
            .document(userId)
            .collection("cursos")
            .document(courseId)
            .addSnapshotListener { snapshot, _ ->
                _courseStatusCurse.value = snapshot?.getString("status") ?: "solicitar"
            }
    }

    fun loadAdminPendingRequests() = viewModelScope.launch {
        adminPendingCount = when (val result = getRequestsUseCase("pendiente")) {
            is Resource.Success -> result.data.size
            is Resource.Error -> 0
            else -> 0
        }
    }

    fun loadUserAcceptedCourses(userId: String) {
        firestore.collection("users")
            .document(userId)
            .collection("cursos")
            .whereEqualTo("status", "aceptado")
            .addSnapshotListener { snapshot, _ ->
                userAcceptedCount = snapshot?.size() ?: 0
            }
    }

    fun initUserStatus(isAdmin: Boolean, userId: String) {
        if (isAdmin) {
            loadAdminPendingRequests()
        } else {
            loadUserAcceptedCourses(userId)
        }
    }

    fun toggleFavorite(userId: String, serviceId: String) {
        viewModelScope.launch {
            val current = _isFavorite.value

            when (
                toggleFavoriteUseCase(
                    userId = userId,
                    courseId = serviceId,
                    isFavorite = current
                )
            ) {
                is Resource.Success -> {
                    _isFavorite.value = !current
                }
                else -> {}
            }
        }
    }


    fun checkIfFavorite(userId: String, serviceId: String) {
        viewModelScope.launch {
            when (val result = getFavoritesUseCase(userId)) {
                is Resource.Success -> {
                    _isFavorite.value = result.data.contains(serviceId)
                }
                else -> {}
            }
        }
    }
}