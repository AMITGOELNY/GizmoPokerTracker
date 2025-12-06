package com.ghn.poker.feature.auth.domain.usecase.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.ghn.poker.core.preferences.PreferenceManager
import com.ghn.poker.feature.auth.domain.repository.LoginRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserStore(
    private val preferenceManager: PreferenceManager,
    private val loginRepository: LoginRepository
) : ViewModel(), Store<AppState> {
    private val _userState: MutableStateFlow<AppState> = MutableStateFlow(AppState.Init)
    override val userState = _userState.asStateFlow()

    override fun checkForToken() {
        if (_userState.value is AppState.Init) {
            viewModelScope.launch {
                preferenceManager.tokenFlow
                    .onEach { Logger.d { "flow updated with $it" } }
                    .collect { token ->
                        val state =
                            if (token.isNullOrBlank()) AppState.LoggedOut else AppState.LoggedIn
                        _userState.update { state }
                    }
            }
        }
    }

    override fun signout() {
        viewModelScope.launch {
            // Call server logout endpoint to revoke all refresh tokens
            loginRepository.logout()
            // Clear local tokens (already done in logout data source, but ensure it's cleared)
            preferenceManager.clearPrefs()
            Logger.d { "User signed out successfully" }
        }
    }
}

interface Store<T> {
    val userState: StateFlow<T>

    fun checkForToken()
    fun signout()
}

sealed class AppState {
    data object Init : AppState()
    data object LoggedIn : AppState()
    data object LoggedOut : AppState()
}

sealed class UserSideEffect {
    data object None : UserSideEffect()
    data object NavigateToHome : UserSideEffect()
    data object NavigateToLogin : UserSideEffect()
}
