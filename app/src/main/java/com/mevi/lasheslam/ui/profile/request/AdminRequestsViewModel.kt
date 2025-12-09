package com.mevi.lasheslam.ui.profile.request

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.usecase.ApproveRequestUseCase
import com.mevi.lasheslam.domain.usecase.GetRequestsUseCase
import com.mevi.lasheslam.domain.usecase.RejectRequestUseCase
import com.mevi.lasheslam.network.CourseRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminRequestsViewModel @Inject constructor(
    private val getRequestsUseCase: GetRequestsUseCase,
    private val approveRequestUseCase: ApproveRequestUseCase,
    private val rejectRequestUseCase: RejectRequestUseCase
) : ViewModel() {

    var requests by mutableStateOf<List<CourseRequest>>(emptyList())
        private set

    var loading by mutableStateOf(false)
        private set

    fun loadRequests(status: String) = viewModelScope.launch {
        loading = true
        when (val result = getRequestsUseCase(status)) {
            is Resource.Success -> requests = result.data
            is Resource.Error -> { /* handle error */ }
            else -> {}
        }
        loading = false
    }

    fun approve(id: String, onDone: () -> Unit) = viewModelScope.launch {
        approveRequestUseCase(id)
        onDone()
    }

    fun reject(id: String, onDone: () -> Unit) = viewModelScope.launch {
        rejectRequestUseCase(id)
        onDone()
    }
}