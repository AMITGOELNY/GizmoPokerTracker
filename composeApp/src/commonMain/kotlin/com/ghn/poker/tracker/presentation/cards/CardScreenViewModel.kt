package com.ghn.poker.tracker.presentation.cards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.ghn.gizmodb.common.models.Card
import com.ghn.gizmodb.common.models.Deck
import com.ghn.poker.tracker.data.sources.remote.ApiResponse
import com.ghn.poker.tracker.domain.usecase.FiveCardSimulatedEvaluationUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CardScreenViewModel(
    private val fiveCardSimulatedEvaluationUseCase: FiveCardSimulatedEvaluationUseCase
) : ViewModel() {
    private val deck: List<CardState> = Deck.cards.map { CardState(it, false) }

    private val _state = MutableStateFlow(CardScreenViewState())
    val state = _state.asStateFlow()

    private val viewStateTrigger = MutableSharedFlow<CardScreenActions>(replay = 1)

    init {
        viewModelScope.launch {
            viewStateTrigger.emit(CardScreenActions.Init)

            viewStateTrigger
                .onEach { Logger.d("CardScreenViewModel") { "Initializing" } }
                .collect { action ->
                    when (action) {
                        CardScreenActions.Init -> distributeCards()
                        CardScreenActions.NewGame -> {
                            delay(500)
                            distributeCards()
                        }
                    }
                }
        }
    }

    private suspend fun distributeCards() {
        val shuffled = deck.shuffled()
        val playerCards = (0 until (state.value.players)).map { playerIndex ->
            (0 until (5)).map { playerCardNumber ->
                shuffled[playerIndex + (state.value.players * playerCardNumber)].card
            }
        }

        _state.update { it.copy(playerCard = playerCards) }

        var winnerInfo = WinnerInfo(-1, "")

        playerCards.forEachIndexed { index, cards ->
            when (val result = fiveCardSimulatedEvaluationUseCase.getFiveCardRank(cards)) {
                is ApiResponse.Error -> Unit // TODO
                is ApiResponse.Success -> {
                    if (result.body < winnerInfo.winningHandValue) {
                        Logger.d { "player: ${index + 1} has high hand" }
                        winnerInfo = winnerInfo.copy(
                            winnerIndex = index,
                            winningHandValue = result.body,
                            winningHand = result.body.getHandRankName
                        )
                    }
                }
            }
        }
        Logger.d { "Player${winnerInfo.winnerIndex + 1} has the high rank: ${winnerInfo.winningHand}" }

        _state.update { it.copy(winnerInfo = winnerInfo) }
    }

    fun dispatch(action: CardScreenActions) {
        viewStateTrigger.tryEmit(action)
    }
}

data class CardScreenViewState(
    val item: String = "",
    val players: Int = 9,
    val playerCard: List<List<Card>> = emptyList(),
    val winnerInfo: WinnerInfo? = null,
)

data class WinnerInfo(
    val winnerIndex: Int,
    val winningHand: String,
    val winningHandValue: Short = Short.MAX_VALUE
)

sealed interface CardScreenActions {
    data object Init : CardScreenActions
    data object NewGame : CardScreenActions
}

data class CardState(val card: Card, val disabled: Boolean)
