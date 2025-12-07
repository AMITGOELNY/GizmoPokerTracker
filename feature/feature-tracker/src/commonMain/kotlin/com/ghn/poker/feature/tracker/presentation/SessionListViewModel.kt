package com.ghn.poker.feature.tracker.presentation

import com.ghn.poker.core.common.presentation.MviViewModel
import com.ghn.poker.core.network.ApiResponse
import com.ghn.poker.feature.tracker.domain.model.SessionData
import com.ghn.poker.feature.tracker.domain.usecase.SessionUseCase
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SessionListViewModel(
    private val useCase: SessionUseCase
) : MviViewModel<SessionListState, SessionListAction, SessionListEffect>() {

    override val initialState = SessionListState()

    init {
        onDispatch(SessionListAction.Init)
    }

    override suspend fun handleAction(action: SessionListAction) {
        when (action) {
            SessionListAction.Init, SessionListAction.Retry -> loadSessions()
        }
    }

    private suspend fun loadSessions() {
        updateState { copy(sessions = LoadableDataState.Loading) }
        when (val sessions = useCase.getSessions()) {
            is ApiResponse.Error -> updateState { copy(sessions = LoadableDataState.Error) }
            is ApiResponse.Success -> updateState {
                copy(
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
