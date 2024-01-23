package com.ghn.poker.tracker.presentation.cards

import co.touchlab.kermit.Logger
import com.ghn.poker.tracker.domain.model.Card
import com.ghn.poker.tracker.domain.model.Deck
import com.ghn.poker.tracker.presentation.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CardScreenViewModel : BaseViewModel() {
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
                        CardScreenActions.NewGame -> distributeCards()
                    }
                }
        }
    }

    private fun distributeCards() {
        val shuffled = deck.shuffled()
        shuffled.onEachIndexed { index, cardState ->
            Logger.d { "index: $index, Card: ${cardState.card}" }
        }
        val playerCards = (0 until (state.value.players)).map { playerIndex ->
            (0 until (5)).map { playerCardNumber ->

                shuffled[playerIndex + (state.value.players * playerCardNumber)].card
            }.also {
                Logger.d { "Card for player: $playerIndex, $it" }
            }
        }

        _state.update { it.copy(playerCard = playerCards) }
    }

    fun dispatch(action: CardScreenActions) {
        viewStateTrigger.tryEmit(action)
    }
}

data class CardScreenViewState(
    val item: String = "",
    val players: Int = 9,
    val playerCard: List<List<Card>> = emptyList(),
)

sealed interface CardScreenActions {
    data object Init : CardScreenActions
    data object NewGame : CardScreenActions
}

data class CardState(val card: Card, val disabled: Boolean)
