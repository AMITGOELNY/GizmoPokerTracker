package com.ghn.poker.tracker.presentation.login

import co.touchlab.kermit.Logger
import com.ghn.poker.tracker.data.sources.remote.ApiResponse
import com.ghn.poker.tracker.domain.usecase.LoginUseCase
import com.ghn.poker.tracker.presentation.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : BaseViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private val viewStateTrigger = MutableSharedFlow<LoginActions>(replay = 1)

    init {
        viewModelScope.launch {
            viewStateTrigger
//                .onEach { localLogger.d { "Triggered new action: $it." } }
                .collect { action ->
                    when (action) {
                        is LoginActions.OnUsernameChange -> onUsernameChange(action.username)
                        is LoginActions.OnPasswordChange -> onPasswordChange(action.password)
                        LoginActions.OnSubmit -> onSubmit()
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
        when (val result = loginUseCase.login(username, password)) {
            is ApiResponse.Error -> {
                _state.update { it.copy(authenticating = false) }
            }

            is ApiResponse.Success -> {
                _state.update { it.copy(authenticating = false) }
            }
        }
    }

    private fun onUsernameChange(username: String) {
        _state.update { it.copy(username = username) }
    }

    private fun onPasswordChange(password: String) {
        _state.update { it.copy(password = password) }
    }

    fun dispatch(action: LoginActions) {
        viewStateTrigger.tryEmit(action)
    }
}

data class LoginState(
    val username: String = "",
    val password: String = "",
    val authenticating: Boolean = false,
)

sealed interface LoginActions {
    data class OnUsernameChange(val username: String) : LoginActions
    data class OnPasswordChange(val password: String) : LoginActions
    data object OnSubmit : LoginActions
}

sealed class LoginEffects
