package com.ghn.poker.feature.cards.presentation

import co.touchlab.kermit.Logger
import com.ghn.gizmodb.common.models.Card
import com.ghn.gizmodb.common.models.CardSuit
import com.ghn.gizmodb.common.models.Deck
import com.ghn.poker.core.common.presentation.MviViewModel
import com.ghn.poker.core.common.util.DecimalFormat
import com.ghn.poker.core.network.ApiResponse
import com.ghn.poker.feature.cards.domain.usecase.EquityCalculationUseCase
import com.ghn.poker.feature.cards.presentation.model.CardState
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.mutate
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class EquityCalculatorViewModel(
    private val calculatorUseCase: EquityCalculationUseCase
) : MviViewModel<EquityCalculatorState, EquityCalculatorAction, EquityCalculatorEffect>() {

    override val initialState = EquityCalculatorState()

    init {
        onDispatch(EquityCalculatorAction.Init)
    }

    override suspend fun handleAction(action: EquityCalculatorAction) {
        when (action) {
            EquityCalculatorAction.Init -> distributeCards()
            is EquityCalculatorAction.BottomSheetUpdate -> bottomSheetUpdate(action)
            is EquityCalculatorAction.OnCardSelected -> onCardSelected(action)
            EquityCalculatorAction.OnConfirmSelected -> onConfirmSelected()
            is EquityCalculatorAction.UpdateSuit -> updateState { copy(selectedSuit = action.cardSuit) }
            EquityCalculatorAction.BottomSheetClose -> updateState { copy(showBottomSheet = false, selectorInfo = null) }
            EquityCalculatorAction.CalculateEquity -> calculateEquity()
        }
    }

    private suspend fun calculateEquity() {
        updateState { copy(isCalculating = true, results = null) }

        val simulationCount = 50000
        val result = calculatorUseCase.getResults(
            heroCards = currentState.heroCard.filterNotNull(),
            boardCardsFiltered = currentState.boardCards.filterNotNull(),
            villainCards = currentState.villainCard.filterNotNull(),
            simulationCount = simulationCount
        )

        when (result) {
            is ApiResponse.Error -> {
                updateState { copy(isCalculating = false) }
            }

            is ApiResponse.Success -> {
                val (hero, villain, tied) = result.body
                val winPercent = hero / simulationCount.toDouble()
                val lossPercent = villain / simulationCount.toDouble()
                val tiePercent = tied / simulationCount.toDouble()

                val formatter = DecimalFormat()
                val winPercentFormatted = formatter.format(winPercent * 100)
                val lossPercentFormatted = formatter.format(lossPercent * 100)
                val tiePercentFormatted = formatter.format(tiePercent * 100)
                Logger.d { "totalCount: ${hero + villain + tied}" }
                Logger.d { "Win: $winPercentFormatted%, Loss: $lossPercentFormatted%" }
                updateState {
                    copy(
                        isCalculating = false,
                        results = HandResults(
                            winPercent = winPercentFormatted,
                            lossPercent = lossPercentFormatted,
                            tiePercent = tiePercentFormatted,
                        )
                    )
                }
            }
        }
    }

    private fun onConfirmSelected() {
        val (type, index, card) = currentState.selectorInfo ?: return
        card ?: error("Can't be null")

        val deck = currentState.deck.mutate {
            val computedIndex = when (card.suit) {
                CardSuit.HEARTS -> card.value - 2 + (13 * 0)
                CardSuit.DIAMONDS -> card.value - 2 + (13 * 1)
                CardSuit.SPADES -> card.value - 2 + (13 * 2)
                CardSuit.CLUBS -> card.value - 2 + (13 * 3)
            }
            it[computedIndex] = it[computedIndex].copy(disabled = true)
        }

        when (type) {
            CardRowType.BOARD -> {
                val cards = currentState.boardCards.mutate { it[index] = card }
                updateState { copy(deck = deck, boardCards = cards) }
            }

            CardRowType.HERO -> {
                val cards = currentState.heroCard.mutate { it[index] = card }
                updateState { copy(deck = deck, heroCard = cards) }
            }

            CardRowType.VILLAIN -> {
                val cards = currentState.villainCard.mutate { it[index] = card }
                updateState { copy(deck = deck, villainCard = cards) }
            }
        }

        updateState { copy(showBottomSheet = false, selectorInfo = null) }
    }

    private fun onCardSelected(action: EquityCalculatorAction.OnCardSelected) {
        val selectedCard = currentState.selectorInfo?.selectedCard
        val deck = currentState.deck.mutate {
            if (selectedCard != null) {
                val computedIndex = when (selectedCard.suit) {
                    CardSuit.HEARTS -> selectedCard.value - 2 + (13 * 0)
                    CardSuit.DIAMONDS -> selectedCard.value - 2 + (13 * 1)
                    CardSuit.SPADES -> selectedCard.value - 2 + (13 * 2)
                    CardSuit.CLUBS -> selectedCard.value - 2 + (13 * 3)
                }
                it[computedIndex] = it[computedIndex].copy(disabled = false)
            }
        }

        updateState {
            copy(
                deck = deck,
                selectorInfo = selectorInfo?.copy(selectedCard = action.card)
            )
        }
    }

    private fun bottomSheetUpdate(action: EquityCalculatorAction.BottomSheetUpdate) {
        val selectedCard = when (action.cardRowType) {
            CardRowType.BOARD -> currentState.boardCards.getOrNull(action.index)
            CardRowType.HERO -> currentState.heroCard.getOrNull(action.index)
            CardRowType.VILLAIN -> currentState.villainCard.getOrNull(action.index)
        }

        updateState {
            copy(
                showBottomSheet = action.showBottomSheet,
                selectorInfo = SelectorInfo(
                    cardRowType = action.cardRowType,
                    index = action.index,
                    selectedCard = selectedCard
                )
            )
        }
    }

    private fun distributeCards() {
        currentState.deck.shuffled()
    }
}

data class EquityCalculatorState(
    val deck: PersistentList<CardState> = Deck.cards.map { CardState(it, false) }
        .toPersistentList(),
    val item: String = "",
    val heroCard: PersistentList<Card?> = persistentListOf(null, null),
    val villainCard: PersistentList<Card?> = persistentListOf(null, null),
    val boardCards: PersistentList<Card?> = persistentListOf(null, null, null, null, null),
    val selectedSuit: CardSuit = CardSuit.HEARTS,
    val showBottomSheet: Boolean = false,
    val selectorInfo: SelectorInfo? = null,
    val isCalculating: Boolean = false,
    val results: HandResults? = null,
) {
    val sheetDisplayCards: List<CardState>
        get() = when (selectedSuit) {
            CardSuit.HEARTS -> deck.subList(0, 13)
            CardSuit.DIAMONDS -> deck.subList(13, 26)
            CardSuit.SPADES -> deck.subList(26, 39)
            CardSuit.CLUBS -> deck.subList(39, 52)
        }

    val calculateEnabled: Boolean
        get() = heroCard.all { it != null } && villainCard.all { it != null }
}

data class SelectorInfo(
    val cardRowType: CardRowType,
    val index: Int,
    val selectedCard: Card? = null
)

data class HandResults(
    val winPercent: String,
    val lossPercent: String,
    val tiePercent: String,
)

sealed interface EquityCalculatorAction {
    data object Init : EquityCalculatorAction
    data class UpdateSuit(val cardSuit: CardSuit) : EquityCalculatorAction
    data class BottomSheetUpdate(
        val showBottomSheet: Boolean,
        val cardRowType: CardRowType,
        val index: Int,
    ) : EquityCalculatorAction

    data class OnCardSelected(val card: Card) : EquityCalculatorAction

    data object OnConfirmSelected : EquityCalculatorAction

    data object BottomSheetClose : EquityCalculatorAction

    data object CalculateEquity : EquityCalculatorAction
}

sealed interface EquityCalculatorEffect

enum class CardRowType(val count: Int) {
    BOARD(5),
    HERO(2),
    VILLAIN(2)
}
