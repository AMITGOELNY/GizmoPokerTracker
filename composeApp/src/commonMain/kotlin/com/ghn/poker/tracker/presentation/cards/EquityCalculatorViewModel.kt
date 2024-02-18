package com.ghn.poker.tracker.presentation.cards

import co.touchlab.kermit.Logger
import com.ghn.poker.tracker.domain.model.Card
import com.ghn.poker.tracker.domain.model.CardSuit
import com.ghn.poker.tracker.domain.model.Deck
import com.ghn.poker.tracker.domain.usecase.EquityCalculationUseCase
import com.ghn.poker.tracker.domain.usecase.impl.EquityCalculationUseCaseImpl.Companion.SIMULATION_COUNT
import com.ghn.poker.tracker.presentation.BaseViewModel
import com.ghn.poker.tracker.util.DecimalFormat
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.mutate
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EquityCalculatorViewModel(
    private val calculatorUseCase: EquityCalculationUseCase
) : BaseViewModel() {

    private val _state = MutableStateFlow(EquityCalculatorViewState())
    val state = _state.asStateFlow()

    private val viewStateTrigger = MutableSharedFlow<EquityCalculatorActions>(replay = 1)

    init {
        viewModelScope.launch {
            viewStateTrigger.emit(EquityCalculatorActions.Init)

            viewStateTrigger
                .onEach { Logger.d("EquityCalculatorViewModel") { "action: $it" } }
                .collect { action ->
                    when (action) {
                        EquityCalculatorActions.Init -> distributeCards()
                        is EquityCalculatorActions.BottomSheetUpdate -> bottomSheetUpdate(action)
                        is EquityCalculatorActions.OnCardSelected -> onCardSelected(action)
                        is EquityCalculatorActions.OnConfirmSelected -> onConfirmSelected()

                        is EquityCalculatorActions.UpdateSuit ->
                            _state.update { it.copy(selectedSuit = action.cardSuit) }

                        EquityCalculatorActions.BottomSheetClose ->
                            _state.update { it.copy(showBottomSheet = false, selectorInfo = null) }

                        EquityCalculatorActions.CalculateEquity -> calculateEquity()
                    }
                }
        }
    }

    private suspend fun calculateEquity() {
        _state.update { it.copy(isCalculating = true, results = null) }

        val result = calculatorUseCase.evaluateCards(
            heroCards = _state.value.heroCard,
            villainCards = _state.value.villainCard,
            deck = _state.value.deck
        )

        val winPercent = result[0] / SIMULATION_COUNT
        val lossPercent = result[1] / SIMULATION_COUNT
        val tiePercent = result[2] / SIMULATION_COUNT

        val formatter = DecimalFormat()
        val winPercentFormatted = formatter.format(winPercent * 100)
        val lossPercentFormatted = formatter.format(lossPercent * 100)
        val tiePercentFormatted = formatter.format(tiePercent * 100)
        Logger.d { "totalCount: ${result.sum()}" }
        Logger.d { "Win: $winPercentFormatted%, Loss: $lossPercentFormatted%" }
        _state.update {
            it.copy(
                isCalculating = false,
                results = HandResults(
                    winPercent = winPercentFormatted,
                    lossPercent = lossPercentFormatted,
                    tiePercent = tiePercentFormatted,
                )
            )
        }
    }

    private fun onConfirmSelected() {
        val (type, index, card) = _state.value.selectorInfo ?: return
        card ?: error("Can't be null")

        val deck = _state.value.deck.mutate {
            val computedIndex = when (card.suit) {
                CardSuit.HEARTS -> card.value - 2 + (13 * 0)
                CardSuit.DIAMONDS -> card.value - 2 + (13 * 1)
                CardSuit.SPADES -> card.value - 2 + (13 * 2)
                CardSuit.CLUBS -> card.value - 2 + (13 * 3)
            }
            it[computedIndex] = it[computedIndex].copy(disabled = true)
        }

        when (type) {
            CardRowType.BOARD -> TODO()
            CardRowType.HERO -> {
                val cards = _state.value.heroCard.mutate { it[index] = card }
                _state.update {
                    it.copy(deck = deck, heroCard = cards)
                }
            }

            CardRowType.VILLAIN -> {
                val cards = _state.value.villainCard.mutate { it[index] = card }
                _state.update {
                    it.copy(deck = deck, villainCard = cards)
                }
            }
        }

        _state.update { it.copy(showBottomSheet = false, selectorInfo = null) }
    }

    private fun onCardSelected(action: EquityCalculatorActions.OnCardSelected) {
        val selectedCard = _state.value.selectorInfo?.selectedCard
        val deck = _state.value.deck.mutate {
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

        _state.update {
            it.copy(
                deck = deck,
                selectorInfo = it.selectorInfo?.copy(selectedCard = action.card)
            )
        }
    }

    private fun bottomSheetUpdate(action: EquityCalculatorActions.BottomSheetUpdate) {
        val selectedCard = when (action.cardRowType) {
            CardRowType.BOARD -> _state.value.boardCards.getOrNull(action.index)
            CardRowType.HERO -> _state.value.heroCard.getOrNull(action.index)
            CardRowType.VILLAIN -> _state.value.villainCard.getOrNull(action.index)
        }

        _state.update {
            it.copy(
                showBottomSheet = action.showBottomSheet,
                selectorInfo = SelectorInfo(
                    cardRowType = action.cardRowType,
                    index = action.index,
                    selectedCard = selectedCard
                )
            )
        }
    }

    private suspend fun distributeCards() {
        val shuffled = _state.value.deck.shuffled()
    }

    fun dispatch(action: EquityCalculatorActions) {
        viewStateTrigger.tryEmit(action)
    }
}

data class EquityCalculatorViewState(
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

sealed interface EquityCalculatorActions {
    data object Init : EquityCalculatorActions
    data class UpdateSuit(val cardSuit: CardSuit) : EquityCalculatorActions
    data class BottomSheetUpdate(
        val showBottomSheet: Boolean,
        val cardRowType: CardRowType,
        val index: Int,
    ) : EquityCalculatorActions

    data class OnCardSelected(val card: Card) : EquityCalculatorActions

    data object OnConfirmSelected : EquityCalculatorActions

    data object BottomSheetClose : EquityCalculatorActions

    data object CalculateEquity : EquityCalculatorActions
}

enum class CardRowType(val count: Int) {
    BOARD(5),
    HERO(2),
    VILLAIN(2)
}
