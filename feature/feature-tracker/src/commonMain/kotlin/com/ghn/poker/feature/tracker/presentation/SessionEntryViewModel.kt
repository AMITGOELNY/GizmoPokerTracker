package com.ghn.poker.feature.tracker.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.ghn.gizmodb.common.models.GameType
import com.ghn.gizmodb.common.models.Venue
import com.ghn.poker.core.common.util.DAY_AND_MONTH_FORMAT
import com.ghn.poker.core.common.util.format
import com.ghn.poker.core.network.ApiResponse
import com.ghn.poker.feature.tracker.domain.usecase.SessionUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.android.annotation.KoinViewModel
import kotlin.time.Clock

// TODO: Migrate to MviViewModel base class once Koin annotations (currently 2.3.3)
//  properly supports iOS targets with IR linking for classes inheriting from generic base classes.
@KoinViewModel
class SessionEntryViewModel(
    private val useCase: SessionUseCase
) : ViewModel() {

    val state: StateFlow<SessionEntryState>
        field = MutableStateFlow(SessionEntryState())

    private val actions = MutableSharedFlow<SessionEntryAction>(replay = 1)

    private val _effects = Channel<SessionEntryEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()
        .shareIn(viewModelScope, SharingStarted.Eagerly)

    init {
        viewModelScope.launch {
            actions.collect { action ->
                Logger.d("SessionEntryViewModel") { "Action: $action" }
                handleAction(action)
            }
        }
    }

    fun onDispatch(action: SessionEntryAction) {
        actions.tryEmit(action)
    }

    private suspend fun handleAction(action: SessionEntryAction) {
        when (action) {
            SessionEntryAction.Init -> Unit
            is SessionEntryAction.UpdateDate -> state.update { it.copy(date = action.date) }
            is SessionEntryAction.UpdateStartAmount -> state.update { it.copy(startAmount = action.startAmount) }
            is SessionEntryAction.UpdateEndAmount -> state.update { it.copy(endAmount = action.endAmount) }
            is SessionEntryAction.UpdateLocation -> TODO()
            is SessionEntryAction.UpdateGameType -> state.update { it.copy(gameType = action.gameType) }
            is SessionEntryAction.UpdateVenue -> state.update { it.copy(venue = action.venue) }
            SessionEntryAction.SaveSession -> saveSession()
        }
    }

    private suspend fun saveSession() {
        state.update { it.copy(isCreatingSession = true) }
        val result = useCase.createSession(
            date = state.value.date,
            startAmount = state.value.startAmount,
            endAmount = state.value.endAmount,
            gameType = state.value.gameType,
            venue = state.value.venue,
        )
        state.update { it.copy(isCreatingSession = false) }
        when (result) {
            is ApiResponse.Error -> Unit
            is ApiResponse.Success -> _effects.send(SessionEntryEffect.OnSessionCreated)
        }
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
        get() = true
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
