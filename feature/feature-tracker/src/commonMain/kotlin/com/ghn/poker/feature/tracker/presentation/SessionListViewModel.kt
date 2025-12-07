package com.ghn.poker.feature.tracker.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.ghn.poker.core.network.ApiResponse
import com.ghn.poker.feature.tracker.domain.model.SessionData
import com.ghn.poker.feature.tracker.domain.usecase.SessionUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

// TODO: Migrate to MviViewModel base class once Koin annotations (currently 2.3.3)
//  properly supports iOS targets with IR linking for classes inheriting from generic base classes.
@KoinViewModel
class SessionListViewModel(
    private val useCase: SessionUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SessionListState())
    val state = _state.asStateFlow()

    private val actions = MutableSharedFlow<SessionListAction>(replay = 1)

    init {
        viewModelScope.launch {
            actions.collect { action ->
                Logger.d("SessionListViewModel") { "Action: $action" }
                handleAction(action)
            }
        }
        onDispatch(SessionListAction.Init)
    }

    fun onDispatch(action: SessionListAction) {
        actions.tryEmit(action)
    }

    private suspend fun handleAction(action: SessionListAction) {
        when (action) {
            SessionListAction.Init, SessionListAction.Retry -> loadSessions()
        }
    }

    private suspend fun loadSessions() {
        _state.update { it.copy(sessions = LoadableDataState.Loading) }
        when (val sessions = useCase.getSessions()) {
            is ApiResponse.Error -> _state.update { it.copy(sessions = LoadableDataState.Error) }
            is ApiResponse.Success -> _state.update {
                it.copy(
                    sessions = if (sessions.body.isNotEmpty()) {
                        LoadableDataState.Loaded(sessions.body)
                    } else {
                        LoadableDataState.Empty
                    }
                )
            }
        }
    }
}

data class SessionListState(
    val sessions: LoadableDataState<List<SessionData>> = LoadableDataState.Loading
)

sealed class LoadableDataState<out T> {
    data object Loading : LoadableDataState<Nothing>()

    data object Empty : LoadableDataState<Nothing>()

    data object Error : LoadableDataState<Nothing>()

    data class Loaded<out T>(val data: T) : LoadableDataState<T>()

    val dataOrNull: T?
        get() = (this as? Loaded<T>)?.data

    val isLoading: Boolean get() = this is Loading
    val isLoaded: Boolean get() = this is Loaded
}

sealed interface SessionListAction {
    data object Init : SessionListAction
    data object Retry : SessionListAction
}

sealed interface SessionListEffect
