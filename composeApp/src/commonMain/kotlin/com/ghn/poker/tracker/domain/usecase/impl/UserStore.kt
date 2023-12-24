package com.ghn.poker.tracker.domain.usecase.impl

import com.ghn.poker.tracker.presentation.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class UserStore() : BaseViewModel() {
    private val _userState: MutableStateFlow<AppState> = MutableStateFlow(AppState.Init)
    val userState = _userState.asStateFlow()

    fun checkForToken() {
//        if (token) {
        _userState.update { AppState.LoggedIn }
//        }
    }
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
