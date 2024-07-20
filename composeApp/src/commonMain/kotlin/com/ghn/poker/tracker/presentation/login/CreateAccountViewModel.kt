package com.ghn.poker.tracker.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.ghn.poker.tracker.data.sources.remote.ApiResponse
import com.ghn.poker.tracker.domain.usecase.CreateAccountUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateAccountViewModel(
    private val createAccountUseCase: CreateAccountUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(CreateAccountState())
    val state = _state.asStateFlow()

    private val viewStateTrigger = MutableSharedFlow<CreateAccountActions>(replay = 1)

    private val _loginEffects = Channel<CreateAccountEffects>()
    val loginEffects = _loginEffects.receiveAsFlow()
        .shareIn(viewModelScope, SharingStarted.Eagerly)

    init {
        viewModelScope.launch {
            viewStateTrigger
                .onEach { Logger.d { "Triggered new action: $it." } }
                .collect { action ->
                    when (action) {
                        is CreateAccountActions.OnUsernameChange -> onUsernameChange(action.username)
                        is CreateAccountActions.OnPasswordChange -> onPasswordChange(action.password)
                        CreateAccountActions.OnSubmit -> onSubmit()
                    }
                }
        }
    }

    private suspend fun onSubmit() {
        Logger.d {
            "login with username: ${_state.value.username}, password: ${_state.value.password}"
        }
        _state.update { it.copy(authenticating = true) }

        val (username, password) = _state.value
        when (val result = createAccountUseCase.create(username, password)) {
            is ApiResponse.Error -> {
                _state.update { it.copy(authenticating = false) }
            }

            is ApiResponse.Success -> {
                _state.update { it.copy(authenticating = false) }
                _loginEffects.trySend(CreateAccountEffects.NavigateToDashboard)
            }
        }
    }

    private fun onUsernameChange(username: String) {
        _state.update { it.copy(username = username) }
    }

    private fun onPasswordChange(password: String) {
        _state.update { it.copy(password = password) }
    }

    fun dispatch(action: CreateAccountActions) {
        viewStateTrigger.tryEmit(action)
    }
}

data class CreateAccountState(
    val username: String = "",
    val password: String = "",
    val authenticating: Boolean = false,
)

sealed interface CreateAccountActions {
    data class OnUsernameChange(val username: String) : CreateAccountActions
    data class OnPasswordChange(val password: String) : CreateAccountActions
    data object OnSubmit : CreateAccountActions
}

sealed interface CreateAccountEffects {
    data object NavigateToDashboard : CreateAccountEffects
}
