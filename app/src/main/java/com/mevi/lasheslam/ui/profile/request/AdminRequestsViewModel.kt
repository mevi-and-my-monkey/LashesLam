package com.mevi.lasheslam.ui.profile.request

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    fun showLoading() {
        _isLoading.value = true
    }

    fun hideLoading() {
        _isLoading.value = false
    }

    fun loadRequests(status: String) = viewModelScope.launch {
        showLoading()
        when (val result = getRequestsUseCase(status)) {
            is Resource.Success -> requests = result.data
            is Resource.Error -> { /* handle error */
            }

            else -> {}
        }
        hideLoading()
    }

    fun approve(id: String, onDone: () -> Unit) = viewModelScope.launch {
        showLoading()
        approveRequestUseCase(id)
        onDone()
        hideLoading()
    }

    fun reject(id: String, onDone: () -> Unit) = viewModelScope.launch {
        showLoading()
        rejectRequestUseCase(id)
        onDone()
        hideLoading()
    }
}