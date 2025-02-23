package com.ghn.poker.tracker.presentation.session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.ghn.poker.tracker.data.sources.remote.ApiResponse
import com.ghn.poker.tracker.domain.usecase.SessionData
import com.ghn.poker.tracker.domain.usecase.SessionUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SessionListViewModel(useCase: SessionUseCase) : ViewModel() {
    private val _state = MutableStateFlow(SessionListState())
    val state = _state.asStateFlow()

    private val viewStateTrigger = MutableSharedFlow<SessionListAction>(replay = 1)

    init {
        viewModelScope.launch {
            viewStateTrigger.emit(SessionListAction.Init)

            viewStateTrigger
                .onEach { Logger.d("SessionListViewModel") { "Triggered new action : $it" } }
                .collect {
                    when (val sessions = useCase.getSessions()) {
                        is ApiResponse.Error ->
                            _state.update { it.copy(sessions = LoadableDataState.Error) }

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
}
