package com.ghn.poker.feature.auth.domain.usecase.impl

import co.touchlab.kermit.Logger
import com.ghn.poker.core.common.presentation.MviViewModel
import com.ghn.poker.core.preferences.PreferenceManager
import com.ghn.poker.feature.auth.domain.repository.LoginRepository
import kotlinx.coroutines.flow.onEach

class MainViewModel(
    private val preferenceManager: PreferenceManager,
    private val loginRepository: LoginRepository
) : MviViewModel<MainState, MainAction, MainEffect>() {

    override val initialState = MainState()

    override suspend fun handleAction(action: MainAction) {
        when (action) {
            MainAction.CheckForToken -> checkForToken()
            MainAction.SignOut -> signOut()
        }
    }

    private suspend fun checkForToken() {
        if (currentState.appState is AppState.Init) {
            preferenceManager.tokenFlow
                .onEach { Logger.d { "flow updated with $it" } }
                .collect { token ->
                    val appState = if (token.isNullOrBlank()) {
                        AppState.LoggedOut
                    } else {
                        AppState.LoggedIn
                    }
                    updateState { copy(appState = appState) }
                }
        }
    }

    private suspend fun signOut() {
        loginRepository.logout()
        preferenceManager.clearPrefs()
        Logger.d { "User signed out successfully" }
    }
}

data class MainState(
    val appState: AppState = AppState.Init
)

sealed interface MainAction {
    data object CheckForToken : MainAction
    data object SignOut : MainAction
}

sealed interface MainEffect

sealed class AppState {
    data object Init : AppState()
    data object LoggedIn : AppState()
    data object LoggedOut : AppState()
}
