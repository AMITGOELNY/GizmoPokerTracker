package com.ghn.poker.tracker.presentation.cards

import co.touchlab.kermit.Logger
import com.ghn.gizmodb.evaluator.models.Evaluator
import com.ghn.gizmodb.evaluator.models.getHandRankName
import com.ghn.poker.tracker.domain.model.Card
import com.ghn.poker.tracker.domain.model.CardSuit
import com.ghn.poker.tracker.domain.model.Deck
import com.ghn.poker.tracker.presentation.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CardScreenHoldEmViewModel : BaseViewModel() {
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
            val cardIds = (cards + boardCards).map {
                val suitValue = when (it.suit) {
                    CardSuit.CLUBS -> 0
                    CardSuit.DIAMONDS -> 1
                    CardSuit.HEARTS -> 2
                    CardSuit.SPADES -> 3
                }
                val convert = ((it.value - 2) * 4) + suitValue
//                Logger.d { "Converting ${it.name}${it.suit} to binary: ${convert.toString(2)}, decimal: $convert" }
                convert
            }

            val evaluator = Evaluator()
            val rank = evaluator.evaluateCards(cardIds)
            if (lowestRank > rank) {
                lowestRank = rank
                lowestRankIndex = index
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
