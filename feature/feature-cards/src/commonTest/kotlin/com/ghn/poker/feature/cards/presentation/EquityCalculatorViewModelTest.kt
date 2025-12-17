package com.ghn.poker.feature.cards.presentation

import com.ghn.gizmodb.common.models.Card
import com.ghn.gizmodb.common.models.CardSuit
import com.ghn.gizmodb.common.models.EvaluatorResponse
import com.ghn.poker.core.network.ApiResponse
import com.ghn.poker.core.testing.BaseViewModelTest
import com.ghn.poker.feature.cards.domain.usecase.EquityCalculationUseCase
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class EquityCalculatorViewModelTest : BaseViewModelTest() {

    private lateinit var calculatorUseCase: EquityCalculationUseCase

    override fun onSetup() {
        calculatorUseCase = mock<EquityCalculationUseCase>()
    }

    @Test
    fun initial_state_has_empty_cards_and_deck() = runTest(testDispatcher) {
        val viewModel = EquityCalculatorViewModel(calculatorUseCase)
        advanceUntilIdle()

        val state = viewModel.state.value
        state.deck.size shouldBe 52
        state.heroCard.all { it == null }.shouldBeTrue()
        state.villainCard.all { it == null }.shouldBeTrue()
        state.boardCards.all { it == null }.shouldBeTrue()
        state.showBottomSheet.shouldBeFalse()
        state.isCalculating.shouldBeFalse()
        state.results.shouldBeNull()
    }

    @Test
    fun dispatch_UpdateSuit_changes_selected_suit() = runTest(testDispatcher) {
        val viewModel = EquityCalculatorViewModel(calculatorUseCase)
        advanceUntilIdle()

        viewModel.state.value.selectedSuit shouldBe CardSuit.HEARTS

        viewModel.onDispatch(EquityCalculatorAction.UpdateSuit(CardSuit.SPADES))
        advanceUntilIdle()

        viewModel.state.value.selectedSuit shouldBe CardSuit.SPADES
    }

    @Test
    fun dispatch_BottomSheetUpdate_shows_bottom_sheet_with_selector_info() = runTest(testDispatcher) {
        val viewModel = EquityCalculatorViewModel(calculatorUseCase)
        advanceUntilIdle()

        viewModel.onDispatch(
            EquityCalculatorAction.BottomSheetUpdate(
                showBottomSheet = true,
                cardRowType = CardRowType.HERO,
                index = 0
            )
        )
        advanceUntilIdle()

        val state = viewModel.state.value
        state.showBottomSheet.shouldBeTrue()
        state.selectorInfo.shouldNotBeNull()
        state.selectorInfo?.cardRowType shouldBe CardRowType.HERO
        state.selectorInfo?.index shouldBe 0
    }

    @Test
    fun dispatch_BottomSheetClose_hides_bottom_sheet() = runTest(testDispatcher) {
        val viewModel = EquityCalculatorViewModel(calculatorUseCase)
        advanceUntilIdle()

        viewModel.onDispatch(
            EquityCalculatorAction.BottomSheetUpdate(
                showBottomSheet = true,
                cardRowType = CardRowType.HERO,
                index = 0
            )
        )
        advanceUntilIdle()

        viewModel.onDispatch(EquityCalculatorAction.BottomSheetClose)
        advanceUntilIdle()

        val state = viewModel.state.value
        state.showBottomSheet.shouldBeFalse()
        state.selectorInfo.shouldBeNull()
    }

    @Test
    fun dispatch_OnCardSelected_updates_selector_info_with_selected_card() = runTest(testDispatcher) {
        val viewModel = EquityCalculatorViewModel(calculatorUseCase)
        advanceUntilIdle()

        viewModel.onDispatch(
            EquityCalculatorAction.BottomSheetUpdate(
                showBottomSheet = true,
                cardRowType = CardRowType.HERO,
                index = 0
            )
        )
        advanceUntilIdle()

        val card = Card(suit = CardSuit.HEARTS, name = "A", value = 14) // Ace of Hearts
        viewModel.onDispatch(EquityCalculatorAction.OnCardSelected(card))
        advanceUntilIdle()

        viewModel.state.value.selectorInfo?.selectedCard shouldBe card
    }

    @Test
    fun dispatch_OnConfirmSelected_adds_card_to_hero_and_disables_in_deck() = runTest(testDispatcher) {
        val viewModel = EquityCalculatorViewModel(calculatorUseCase)
        advanceUntilIdle()

        viewModel.onDispatch(
            EquityCalculatorAction.BottomSheetUpdate(
                showBottomSheet = true,
                cardRowType = CardRowType.HERO,
                index = 0
            )
        )
        advanceUntilIdle()

        val card = Card(suit = CardSuit.HEARTS, name = "A", value = 14) // Ace of Hearts
        viewModel.onDispatch(EquityCalculatorAction.OnCardSelected(card))
        advanceUntilIdle()

        viewModel.onDispatch(EquityCalculatorAction.OnConfirmSelected)
        advanceUntilIdle()

        val state = viewModel.state.value
        state.heroCard[0] shouldBe card
        state.showBottomSheet.shouldBeFalse()
        // Card should be disabled in deck
        val aceOfHeartsInDeck = state.deck.find { it.card == card }
        aceOfHeartsInDeck?.disabled.shouldBeTrue()
    }

    @Test
    fun dispatch_CalculateEquity_calls_use_case_and_updates_results() = runTest(testDispatcher) {
        val response = EvaluatorResponse(
            heroResult = 25000,
            villainResult = 20000,
            tiedResult = 5000
        )

        everySuspend {
            calculatorUseCase.getResults(any(), any(), any(), any())
        } returns ApiResponse.Success(response)

        val viewModel = EquityCalculatorViewModel(calculatorUseCase)
        advanceUntilIdle()

        // Set up hero cards
        setupCard(viewModel, CardRowType.HERO, 0, Card(CardSuit.HEARTS, "A", 14))
        setupCard(viewModel, CardRowType.HERO, 1, Card(CardSuit.HEARTS, "K", 13))

        // Set up villain cards
        setupCard(viewModel, CardRowType.VILLAIN, 0, Card(CardSuit.SPADES, "Q", 12))
        setupCard(viewModel, CardRowType.VILLAIN, 1, Card(CardSuit.SPADES, "J", 11))

        viewModel.onDispatch(EquityCalculatorAction.CalculateEquity)
        advanceUntilIdle()

        val state = viewModel.state.value
        state.isCalculating.shouldBeFalse()
        state.results.shouldNotBeNull()

        verifySuspend { calculatorUseCase.getResults(any(), any(), any(), any()) }
    }

    @Test
    fun calculateEnabled_returns_false_when_hero_cards_incomplete() = runTest(testDispatcher) {
        val viewModel = EquityCalculatorViewModel(calculatorUseCase)
        advanceUntilIdle()

        viewModel.state.value.calculateEnabled.shouldBeFalse()
    }

    @Test
    fun calculateEnabled_returns_true_when_hero_and_villain_cards_complete() = runTest(testDispatcher) {
        val viewModel = EquityCalculatorViewModel(calculatorUseCase)
        advanceUntilIdle()

        // Set up hero cards
        setupCard(viewModel, CardRowType.HERO, 0, Card(CardSuit.HEARTS, "A", 14))
        setupCard(viewModel, CardRowType.HERO, 1, Card(CardSuit.HEARTS, "K", 13))

        // Set up villain cards
        setupCard(viewModel, CardRowType.VILLAIN, 0, Card(CardSuit.SPADES, "Q", 12))
        setupCard(viewModel, CardRowType.VILLAIN, 1, Card(CardSuit.SPADES, "J", 11))

        viewModel.state.value.calculateEnabled.shouldBeTrue()
    }

    @Test
    fun sheetDisplayCards_returns_correct_suit_cards() = runTest(testDispatcher) {
        val viewModel = EquityCalculatorViewModel(calculatorUseCase)
        advanceUntilIdle()

        viewModel.onDispatch(EquityCalculatorAction.UpdateSuit(CardSuit.HEARTS))
        advanceUntilIdle()

        val heartsCards = viewModel.state.value.sheetDisplayCards
        heartsCards.size shouldBe 13
        heartsCards.all { it.card.suit == CardSuit.HEARTS }.shouldBeTrue()

        viewModel.onDispatch(EquityCalculatorAction.UpdateSuit(CardSuit.SPADES))
        advanceUntilIdle()

        val spadesCards = viewModel.state.value.sheetDisplayCards
        spadesCards.size shouldBe 13
        spadesCards.all { it.card.suit == CardSuit.SPADES }.shouldBeTrue()
    }

    private fun TestScope.setupCard(
        viewModel: EquityCalculatorViewModel,
        rowType: CardRowType,
        index: Int,
        card: Card
    ) {
        viewModel.onDispatch(
            EquityCalculatorAction.BottomSheetUpdate(
                showBottomSheet = true,
                cardRowType = rowType,
                index = index
            )
        )
        advanceUntilIdle()

        viewModel.onDispatch(EquityCalculatorAction.OnCardSelected(card))
        advanceUntilIdle()

        viewModel.onDispatch(EquityCalculatorAction.OnConfirmSelected)
        advanceUntilIdle()
    }
}
