package com.ghn.poker.feature.cards.domain.usecase.impl

import com.ghn.gizmodb.common.models.Card
import com.ghn.gizmodb.common.models.CardSuit
import com.ghn.gizmodb.common.models.EvaluatorResponse
import com.ghn.poker.core.network.ApiResponse
import com.ghn.poker.feature.cards.domain.repository.HandEvaluatorRepository
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class EquityCalculationUseCaseImplTest {

    @Test
    fun getResults_delegates_to_repository_and_returns_response() = runTest {
        val evaluatorRepository = mock<HandEvaluatorRepository>()
        val useCase = EquityCalculationUseCaseImpl(evaluatorRepository)
        val heroCards = listOf(Card(CardSuit.SPADES, "A", 14), Card(CardSuit.HEARTS, "K", 13))
        val boardCards = emptyList<Card>()
        val villainCards = listOf(Card(CardSuit.CLUBS, "Q", 12), Card(CardSuit.DIAMONDS, "J", 11))
        val simulationCount = 1_000
        val expected = ApiResponse.Success(
            EvaluatorResponse(heroResult = 55, villainResult = 40, tiedResult = 5)
        )

        everySuspend {
            evaluatorRepository.evaluate(
                heroCards = heroCards,
                boardCardsFiltered = boardCards,
                villainCards = villainCards,
                simulationCount = simulationCount
            )
        } returns expected

        val result = useCase.getResults(heroCards, boardCards, villainCards, simulationCount)

        result shouldBe expected
        verifySuspend {
            evaluatorRepository.evaluate(
                heroCards = heroCards,
                boardCardsFiltered = boardCards,
                villainCards = villainCards,
                simulationCount = simulationCount
            )
        }
    }
}
