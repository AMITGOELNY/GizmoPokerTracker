package com.ghn.poker.core.common.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

/**
 * Abstract base ViewModel that enforces MVI (Model-View-Intent) pattern.
 *
 * @param State The UI state type that represents the current state of the screen.
 * @param Action The action/intent type that represents user interactions.
 * @param Effect The effect type for one-time events (navigation, toasts, etc.).
 */
abstract class MviViewModel<State, Action, Effect> : ViewModel() {

    protected abstract val initialState: State

    private val _state by lazy { MutableStateFlow(initialState) }
    val state: StateFlow<State> by lazy { _state.asStateFlow() }

    private val actions = MutableSharedFlow<Action>(replay = 1)

    private val _effects = Channel<Effect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()
        .shareIn(viewModelScope, SharingStarted.Eagerly)

    private val tag: String by lazy { this::class.simpleName ?: "MviViewModel" }

    init {
        viewModelScope.launch {
            actions.collect { action ->
                Logger.d(tag) { "Action: $action" }
                handleAction(action)
            }
        }
    }

    /**
     * The single public entry point for dispatching actions.
     * Call this from the UI to send user intents to the ViewModel.
     */
    fun onDispatch(action: Action) {
        actions.tryEmit(action)
    }

    /**
     * Override this to handle each action type.
     * This is called for every action dispatched via [onDispatch].
     */
    protected abstract suspend fun handleAction(action: Action)

    /**
     * Update the current state using a reducer function.
     */
    protected fun updateState(reducer: State.() -> State) {
        _state.value = _state.value.reducer()
    }

    /**
     * Get the current state value.
     */
    protected val currentState: State
        get() = _state.value

    /**
     * Emit a one-time effect (navigation, snackbar, etc.).
     */
    protected fun emitEffect(effect: Effect) {
        viewModelScope.launch {
            _effects.send(effect)
        }
    }

    /**
     * Launch a coroutine in the viewModelScope.
     * Use this for long-running operations that shouldn't block action handling.
     */
    protected fun launchInViewModel(block: suspend () -> Unit) {
        viewModelScope.launch {
            block()
        }
    }
}
