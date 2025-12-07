package com.ghn.poker.feature.auth.presentation

import co.touchlab.kermit.Logger
import com.ghn.poker.core.common.presentation.MviViewModel
import com.ghn.poker.core.network.ApiResponse
import com.ghn.poker.feature.auth.domain.usecase.CreateAccountUseCase
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class CreateAccountViewModel(
    private val createAccountUseCase: CreateAccountUseCase
) : MviViewModel<CreateAccountState, CreateAccountAction, CreateAccountEffect>() {

    override val initialState = CreateAccountState()

    override suspend fun handleAction(action: CreateAccountAction) {
        when (action) {
            is CreateAccountAction.OnUsernameChange -> updateState { copy(username = action.username) }
            is CreateAccountAction.OnPasswordChange -> updateState { copy(password = action.password) }
            CreateAccountAction.OnSubmit -> onSubmit()
        }
    }

    private suspend fun onSubmit() {
        Logger.d {
            "login with username: ${currentState.username}, password: ${currentState.password}"
        }
        updateState { copy(authenticating = true) }

        val (username, password) = currentState
        when (val result = createAccountUseCase.create(username, password)) {
            is ApiResponse.Error -> {
                updateState { copy(authenticating = false) }
            }

            is ApiResponse.Success -> {
                updateState { copy(authenticating = false) }
                emitEffect(CreateAccountEffect.NavigateToDashboard)
            }
        }
    }
}

data class CreateAccountState(
    val username: String = "",
    val password: String = "",
    val authenticating: Boolean = false,
)

sealed interface CreateAccountAction {
    data class OnUsernameChange(val username: String) : CreateAccountAction
    data class OnPasswordChange(val password: String) : CreateAccountAction
    data object OnSubmit : CreateAccountAction
}

sealed interface CreateAccountEffect {
    data object NavigateToDashboard : CreateAccountEffect
}
