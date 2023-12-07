package com.ghn.poker.tracker.presentation.session

import co.touchlab.kermit.Logger
import com.ghn.poker.tracker.domain.usecase.SessionUseCase
import com.ghn.poker.tracker.presentation.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SessionEntryViewModel : BaseViewModel(), KoinComponent {
    private val _state = MutableStateFlow(SessionEntryState())
    val state = _state.asStateFlow()

    private val viewStateTrigger = MutableSharedFlow<SessionEntryAction>(replay = 1)

    private val useCase: SessionUseCase by inject<SessionUseCase>()

    init {
        viewModelScope.launch {
            viewStateTrigger
                .onEach { Logger.d { "Triggered new action : $it" } }
                .collect { action ->
                    when (action) {
                        is SessionEntryAction.UpdateDate -> TODO()
                        is SessionEntryAction.UpdateStartAmount -> updateStartAmount(action)
                        is SessionEntryAction.UpdateEndAmount -> updateEndAmount(action)
                        is SessionEntryAction.UpdateLocation -> TODO()
                        is SessionEntryAction.SaveSession -> saveSession()
                    }
                }
        }
    }

    private suspend fun saveSession() {
        useCase.insertSession(state.value.date, state.value.startAmount, state.value.endAmount)
    }

    private fun updateStartAmount(action: SessionEntryAction.UpdateStartAmount) {
        _state.update { it.copy(startAmount = action.startAmount) }
    }

    private fun updateEndAmount(action: SessionEntryAction.UpdateEndAmount) {
        _state.update { it.copy(startAmount = action.endAmount) }
    }

    fun dispatch(action: SessionEntryAction) {
        viewStateTrigger.tryEmit(action)
    }
}

data class SessionEntryState(
    val date: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    val startAmount: Double? = null,
    val endAmount: Double? = null,
    val location: String? = null,
    val coordinates: GeoCoordinates? = null
) {
    val saveEnabled: Boolean
        get() = true // (startAmount == null && endAmount == null).not()
}

sealed class SessionEntryAction {
    data class UpdateDate(val date: LocalDateTime) : SessionEntryAction()

    data class UpdateStartAmount(val startAmount: Double?) : SessionEntryAction()

    data class UpdateEndAmount(val endAmount: Double?) : SessionEntryAction()

    data class UpdateLocation(val location: String?) : SessionEntryAction()

    data object SaveSession : SessionEntryAction()
}

data class GeoCoordinates(val latitude: Double, val longitude: Double)
