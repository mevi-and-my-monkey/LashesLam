package com.mevi.lasheslam

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

abstract class BaseViewModel <State, Event> : ViewModel() {

    protected abstract fun createInitialState(): State

    private val _uiState = MutableStateFlow(createInitialState())
    val uiState: StateFlow<State> = _uiState

    private val _events = MutableSharedFlow<Event>()
    val events: SharedFlow<Event> = _events

    protected fun setState(reducer: State.() -> State) {
        _uiState.update { it.reducer() }
    }

    protected suspend fun sendEvent(event: Event) {
        _events.emit(event)
    }
}