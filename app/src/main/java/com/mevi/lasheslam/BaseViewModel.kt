package com.mevi.lasheslam

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mevi.lasheslam.core.error.AppError
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.ui.common.UiState
import com.mevi.lasheslam.ui.common.toUserMessage
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<State : UiState<State>, Event> : ViewModel() {

    protected abstract fun createInitialState(): State

    private val _uiState = MutableStateFlow(createInitialState())
    val uiState: StateFlow<State> = _uiState

    private val _events = Channel<Event>()
    val events = _events.receiveAsFlow()

    protected fun setState(reducer: State.() -> State) {
        _uiState.update { it.reducer() }
    }

    protected suspend fun sendEvent(event: Event) {
        _events.send(event)
    }

    private var loadingCount = 0

    protected fun launchWithLoading(block: suspend () -> Unit) {
        viewModelScope.launch {
            loadingCount++
            setState { copyWithLoading(true) }

            try {
                block()
            } finally {
                loadingCount--
                if (loadingCount == 0) {
                    setState { copyWithLoading(false) }
                }
            }
        }
    }

    protected suspend fun <T> handleResult(
        result: Resource<T>,
        onSuccess: suspend (T) -> Unit,
        onError: suspend (AppError) -> Unit
    ) {
        when (result) {
            is Resource.Success -> onSuccess(result.data)

            is Resource.Error -> {
                onError(result.error)
            }
        }
    }

    protected suspend fun sendError(
        error: AppError,
        mapper: (String) -> Event
    ) {
        val message = error.toUserMessage()
        sendEvent(mapper(message))
    }
}