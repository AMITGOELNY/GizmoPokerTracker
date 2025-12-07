package com.ghn.poker.feature.auth.presentation

import co.touchlab.kermit.Logger
import com.ghn.poker.core.common.presentation.MviViewModel
import com.ghn.poker.core.network.ApiResponse
import com.ghn.poker.feature.auth.domain.usecase.LoginUseCase
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : MviViewModel<LoginState, LoginAction, LoginEffect>() {

    override val initialState = LoginState()

    override suspend fun handleAction(action: LoginAction) {
        when (action) {
            is LoginAction.OnUsernameChange -> updateState { copy(username = action.username) }
            is LoginAction.OnPasswordChange -> updateState { copy(password = action.password) }
            LoginAction.OnSubmit -> onSubmit()
        }
    }

    private suspend fun onSubmit() {
        Logger.d {
            "login with username: ${currentState.username}, password: ${currentState.password}"
        }
        updateState { copy(authenticating = true) }

        val (username, password) = currentState
        when (val result = loginUseCase.login(username, password)) {
            is ApiResponse.Error -> {
                updateState { copy(authenticating = false) }
            }

            is ApiResponse.Success -> {
                updateState { copy(authenticating = false) }
                emitEffect(LoginEffect.NavigateToDashboard)
            }
        }
    }
}

data class LoginState(
    val username: String = "",
    val password: String = "",
    val authenticating: Boolean = false,
)

sealed interface LoginAction {
    data class OnUsernameChange(val username: String) : LoginAction
    data class OnPasswordChange(val password: String) : LoginAction
    data object OnSubmit : LoginAction
}

sealed interface LoginEffect {
    data object NavigateToDashboard : LoginEffect
}
