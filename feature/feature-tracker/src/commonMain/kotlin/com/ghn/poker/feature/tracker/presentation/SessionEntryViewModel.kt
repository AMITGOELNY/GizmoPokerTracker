package com.ghn.poker.feature.tracker.presentation

import com.ghn.gizmodb.common.models.GameType
import com.ghn.gizmodb.common.models.Venue
import com.ghn.poker.core.common.presentation.MviViewModel
import com.ghn.poker.core.common.util.DAY_AND_MONTH_FORMAT
import com.ghn.poker.core.common.util.format
import com.ghn.poker.core.network.ApiResponse
import com.ghn.poker.feature.tracker.domain.usecase.SessionUseCase
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.android.annotation.KoinViewModel
import kotlin.time.Clock

@KoinViewModel
class SessionEntryViewModel(
    private val useCase: SessionUseCase
) : MviViewModel<SessionEntryState, SessionEntryAction, SessionEntryEffect>() {

    override val initialState = SessionEntryState()

    override suspend fun handleAction(action: SessionEntryAction) {
        when (action) {
            SessionEntryAction.Init -> Unit
            is SessionEntryAction.UpdateDate -> updateState { copy(date = action.date) }
            is SessionEntryAction.UpdateStartAmount -> updateState { copy(startAmount = action.startAmount) }
            is SessionEntryAction.UpdateEndAmount -> updateState { copy(endAmount = action.endAmount) }
            is SessionEntryAction.UpdateLocation -> TODO()
            is SessionEntryAction.UpdateGameType -> updateState { copy(gameType = action.gameType) }
            is SessionEntryAction.UpdateVenue -> updateState { copy(venue = action.venue) }
            SessionEntryAction.SaveSession -> saveSession()
        }
    }

    private suspend fun saveSession() {
        updateState { copy(isCreatingSession = true) }
        val result = useCase.createSession(
            date = currentState.date,
            startAmount = currentState.startAmount,
            endAmount = currentState.endAmount,
            gameType = currentState.gameType,
            venue = currentState.venue,
        )
        updateState { copy(isCreatingSession = false) }
        when (result) {
            is ApiResponse.Error -> Unit
            is ApiResponse.Success -> emitEffect(SessionEntryEffect.OnSessionCreated)
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
