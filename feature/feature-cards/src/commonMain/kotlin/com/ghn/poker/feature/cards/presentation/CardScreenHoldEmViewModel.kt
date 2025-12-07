package com.ghn.poker.feature.cards.presentation

import co.touchlab.kermit.Logger
import com.ghn.gizmodb.common.models.Card
import com.ghn.gizmodb.common.models.Deck
import com.ghn.poker.core.common.presentation.MviViewModel
import com.ghn.poker.core.network.ApiResponse
import com.ghn.poker.feature.cards.domain.usecase.FiveCardSimulatedEvaluationUseCase
import com.ghn.poker.feature.cards.presentation.model.CardState
import kotlinx.coroutines.delay
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class CardScreenHoldEmViewModel(
    private val fiveCardSimulatedEvaluationUseCase: FiveCardSimulatedEvaluationUseCase
) : MviViewModel<CardScreenHoldEmState, CardScreenHoldEmAction, CardScreenHoldEmEffect>() {

    private val deck: List<CardState> = Deck.cards.map { CardState(it, false) }

    override val initialState = CardScreenHoldEmState()

    init {
        onDispatch(CardScreenHoldEmAction.Init)
    }

    override suspend fun handleAction(action: CardScreenHoldEmAction) {
        when (action) {
            CardScreenHoldEmAction.Init -> distributeCards()
            CardScreenHoldEmAction.NewGame -> {
                delay(500)
                distributeCards()
            }
        }
    }

    private suspend fun distributeCards() {
        val shuffled = deck.shuffled()
        val playerCards = (0 until (currentState.players)).map { playerIndex ->
            (0 until (2)).map { playerCardNumber ->
                shuffled[playerIndex + (currentState.players * playerCardNumber)].card
            }
        }

        updateState { copy(playerCard = playerCards) }

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

        val winnerInfo = if (lowestRankIndex >= 0) {
            WinnerInfo(
                winnerIndex = lowestRankIndex,
                winningHand = lowestRank.getHandRankName,
                winningHandValue = lowestRank
            )
        } else {
            null
        }

        updateState { copy(boardCards = boardCards, winnerInfo = winnerInfo) }
    }
}

data class CardScreenHoldEmState(
    val item: String = "",
    val players: Int = 6,
    val playerCard: List<List<Card>> = emptyList(),
    val boardCards: List<Card> = emptyList(),
    val winnerInfo: WinnerInfo? = null,
)

sealed interface CardScreenHoldEmAction {
    data object Init : CardScreenHoldEmAction
    data object NewGame : CardScreenHoldEmAction
}

sealed interface CardScreenHoldEmEffect
