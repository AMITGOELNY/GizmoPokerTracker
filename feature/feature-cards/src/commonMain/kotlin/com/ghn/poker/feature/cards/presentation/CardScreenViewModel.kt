package com.ghn.poker.feature.cards.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.ghn.gizmodb.common.models.Card
import com.ghn.gizmodb.common.models.Deck
import com.ghn.poker.core.network.ApiResponse
import com.ghn.poker.feature.cards.domain.usecase.FiveCardSimulatedEvaluationUseCase
import com.ghn.poker.feature.cards.presentation.model.CardState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

// TODO: Migrate to MviViewModel base class once Koin annotations (currently 2.3.3)
//  properly supports iOS targets with IR linking for classes inheriting from generic base classes.
@KoinViewModel
class CardScreenViewModel(
    private val fiveCardSimulatedEvaluationUseCase: FiveCardSimulatedEvaluationUseCase
) : ViewModel() {

    private val deck: List<CardState> = Deck.cards.map { CardState(it, false) }

    val state: StateFlow<CardScreenState>
        field = MutableStateFlow(CardScreenState())

    private val actions = MutableSharedFlow<CardScreenAction>(replay = 1)

    init {
        viewModelScope.launch {
            actions.collect { action ->
                Logger.d("CardScreenViewModel") { "Action: $action" }
                handleAction(action)
            }
        }
        onDispatch(CardScreenAction.Init)
    }

    fun onDispatch(action: CardScreenAction) {
        actions.tryEmit(action)
    }

    private suspend fun handleAction(action: CardScreenAction) {
        when (action) {
            CardScreenAction.Init -> distributeCards()
            CardScreenAction.NewGame -> {
                delay(500)
                distributeCards()
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

        state.update { it.copy(playerCard = playerCards) }

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

        state.update { it.copy(winnerInfo = winnerInfo) }
    }
}

data class CardScreenState(
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

sealed interface CardScreenAction {
    data object Init : CardScreenAction
    data object NewGame : CardScreenAction
}

sealed interface CardScreenEffect

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
