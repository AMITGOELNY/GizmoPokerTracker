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
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class CardScreenHoldEmViewModel(
    private val fiveCardSimulatedEvaluationUseCase: FiveCardSimulatedEvaluationUseCase
) : ViewModel() {
    private val deck: List<CardState> = Deck.cards.map { CardState(it, false) }

    private val _state = MutableStateFlow(CardScreenHoldEmViewState())
    val state = _state.asStateFlow()

    private val viewStateTrigger = MutableSharedFlow<CardScreenHoldEmActions>(replay = 1)

    init {
        viewModelScope.launch {
            viewStateTrigger.emit(CardScreenHoldEmActions.Init)

            viewStateTrigger
                .onEach { Logger.d("CardScreenHoldEmViewModel") { "Initializing" } }
                .collect { action ->
                    when (action) {
                        CardScreenHoldEmActions.Init -> distributeCards()
                        CardScreenHoldEmActions.NewGame -> {
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
            (0 until (2)).map { playerCardNumber ->
                shuffled[playerIndex + (state.value.players * playerCardNumber)].card
            }
        }

        _state.update { it.copy(playerCard = playerCards) }

        delay(2000)
        val startIndex = (playerCards.count() * 2) // dealt index
        val boardCards = (startIndex..(startIndex + 4)).map {
            shuffled[it].card
        }

        var lowestRank = Short.MAX_VALUE
        var lowestRankIndex = -1
        playerCards.forEachIndexed { index, cards ->
            when (val result = fiveCardSimulatedEvaluationUseCase.getFiveCardRank(cards + boardCards)) {
                is ApiResponse.Error -> {}
                is ApiResponse.Success -> {
                    if (lowestRank > result.body) {
                        lowestRank = result.body
                        lowestRankIndex = index
                    }
                }
            }
        }

        Logger.d { "player: ${lowestRankIndex + 1}, rank: $lowestRank, Best Hand: ${lowestRank.getHandRankName}" }
        _state.update { it.copy(boardCards = boardCards) }
    }

    fun dispatch(action: CardScreenHoldEmActions) {
        viewStateTrigger.tryEmit(action)
    }
}

data class CardScreenHoldEmViewState(
    val item: String = "",
    val players: Int = 6,
    val playerCard: List<List<Card>> = emptyList(),
    val boardCards: List<Card> = emptyList(),
    val winnerInfo: WinnerInfo? = null,
)

sealed interface CardScreenHoldEmActions {
    data object Init : CardScreenHoldEmActions
    data object NewGame : CardScreenHoldEmActions
}

val Short.getHandRankName: String
    get() = when {
        this > 6185 -> "HIGH CARD"
        this > 3325 -> "ONE PAIR"
        this > 2467 -> "TWO PAIR"
        this > 1609 -> "THREE OF A KIND"
        this > 1599 -> "STRAIGHT"
        this > 322 -> "FLUSH"
        this > 166 -> "FULL HOUSE"
        this > 10 -> "FOUR OF A KIND"
        else -> "STRAIGHT FLUSH"
    }
