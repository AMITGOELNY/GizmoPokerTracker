package com.ghn.poker.feature.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.ghn.poker.core.network.ApiResponse
import com.ghn.poker.feature.auth.domain.usecase.CreateAccountUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

// TODO: Migrate to MviViewModel base class once Koin annotations (currently 2.3.3)
//  properly supports iOS targets with IR linking for classes inheriting from generic base classes.
@KoinViewModel
class CreateAccountViewModel(
    private val createAccountUseCase: CreateAccountUseCase
) : ViewModel() {

    val state: StateFlow<CreateAccountState>
        field = MutableStateFlow(CreateAccountState())

    private val actions = MutableSharedFlow<CreateAccountAction>(replay = 1)

    private val _effects = Channel<CreateAccountEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()
        .shareIn(viewModelScope, SharingStarted.Eagerly)

    init {
        viewModelScope.launch {
            actions.collect { action ->
                Logger.d("CreateAccountViewModel") { "Action: $action" }
                handleAction(action)
            }
        }
    }

    fun onDispatch(action: CreateAccountAction) {
        actions.tryEmit(action)
    }

    private suspend fun handleAction(action: CreateAccountAction) {
        when (action) {
            is CreateAccountAction.OnUsernameChange -> state.update { it.copy(username = action.username) }
            is CreateAccountAction.OnPasswordChange -> state.update { it.copy(password = action.password) }
            CreateAccountAction.OnSubmit -> onSubmit()
        }
    }

    private suspend fun onSubmit() {
        Logger.d {
            "login with username: ${state.value.username}, password: ${state.value.password}"
        }
        state.update { it.copy(authenticating = true) }

        val (username, password) = state.value
        when (val result = createAccountUseCase.create(username, password)) {
            is ApiResponse.Error -> {
                state.update { it.copy(authenticating = false) }
            }

            is ApiResponse.Success -> {
                state.update { it.copy(authenticating = false) }
                _effects.send(CreateAccountEffect.NavigateToDashboard)
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
