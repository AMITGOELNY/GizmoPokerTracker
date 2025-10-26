package com.ghn.poker.tracker.presentation.session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.ghn.gizmodb.common.models.GameType
import com.ghn.gizmodb.common.models.Venue
import com.ghn.poker.tracker.data.sources.remote.ApiResponse
import com.ghn.poker.tracker.domain.usecase.SessionUseCase
import com.ghn.poker.tracker.util.DAY_AND_MONTH_FORMAT
import com.ghn.poker.tracker.util.format
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.android.annotation.KoinViewModel
import kotlin.time.Clock

@KoinViewModel
class SessionEntryViewModel(
    private val useCase: SessionUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(SessionEntryState())
    val state = _state.asStateFlow()

    private val viewStateTrigger = MutableSharedFlow<SessionEntryAction>(replay = 1)

    private val _effects = Channel<SessionEntryEffect>()
    val effects = _effects.receiveAsFlow().shareIn(viewModelScope, SharingStarted.Eagerly)

    init {
        viewModelScope.launch {
            viewStateTrigger.emit(SessionEntryAction.Init)

            viewStateTrigger
                .onEach { Logger.d("SessionEntryViewModel") { "Triggered new action : $it" } }
                .collect { action ->
                    when (action) {
                        is SessionEntryAction.UpdateDate -> updateDate(action.date)
                        is SessionEntryAction.UpdateStartAmount -> updateStartAmount(action)
                        is SessionEntryAction.UpdateEndAmount -> updateEndAmount(action)
                        is SessionEntryAction.UpdateLocation -> TODO()
                        is SessionEntryAction.SaveSession -> saveSession()
                        is SessionEntryAction.UpdateGameType ->
                            _state.update { it.copy(gameType = action.gameType) }

                        is SessionEntryAction.UpdateVenue ->
                            _state.update { it.copy(venue = action.venue) }

                        SessionEntryAction.Init -> Unit
                    }
                }
        }
    }

    private fun updateDate(date: kotlin.time.Instant) {
        _state.update { it.copy(date = date) }
    }

    private suspend fun saveSession() {
        _state.update { it.copy(isCreatingSession = true) }
        val result = useCase.createSession(
            date = state.value.date,
            startAmount = state.value.startAmount,
            endAmount = state.value.endAmount,
            gameType = state.value.gameType,
            venue = state.value.venue,
        )
        _state.update { it.copy(isCreatingSession = false) }
        when (result) {
            is ApiResponse.Error -> Unit
            is ApiResponse.Success -> _effects.send(SessionEntryEffect.OnSessionCreated)
        }
//        useCase.insertSession(state.value.date, state.value.startAmount, state.value.endAmount)
    }

    private fun updateStartAmount(action: SessionEntryAction.UpdateStartAmount) {
        _state.update { it.copy(startAmount = action.startAmount) }
    }

    private fun updateEndAmount(action: SessionEntryAction.UpdateEndAmount) {
        _state.update { it.copy(endAmount = action.endAmount) }
    }

    fun dispatch(action: SessionEntryAction) {
        viewStateTrigger.tryEmit(action)
    }
}

data class SessionEntryState(
    val date: kotlin.time.Instant = Clock.System.now(),
    val startAmount: Double? = null,
    val endAmount: Double? = null,
    val gameType: GameType = GameType.PLO_2_2,
    val venue: Venue = Venue.HARD_ROCK_FL,
    val location: String? = null,
    val coordinates: GeoCoordinates? = null,
    val isCreatingSession: Boolean = false,
) {
    val dateFormatted: String
        get() = date.toLocalDateTime(TimeZone.UTC).format(DAY_AND_MONTH_FORMAT).orEmpty()

    val saveEnabled: Boolean
        get() = true // (startAmount == null && endAmount == null).not()
}

sealed interface SessionEntryAction {
    data object Init : SessionEntryAction

    data class UpdateDate(val date: kotlin.time.Instant) : SessionEntryAction

    data class UpdateStartAmount(val startAmount: Double?) : SessionEntryAction

    data class UpdateEndAmount(val endAmount: Double?) : SessionEntryAction

    data class UpdateLocation(val location: String?) : SessionEntryAction

    data class UpdateGameType(val gameType: GameType) : SessionEntryAction

    data class UpdateVenue(val venue: Venue) : SessionEntryAction

    data object SaveSession : SessionEntryAction
}

sealed interface SessionEntryEffect {
    data object OnSessionCreated : SessionEntryEffect
}

data class GeoCoordinates(val latitude: Double, val longitude: Double)
