package com.ghn.poker.feature.cards.data.repository

import com.ghn.gizmodb.common.models.Card
import com.ghn.gizmodb.common.models.CardSuit
import com.ghn.gizmodb.common.models.EvaluatorResponse
import com.ghn.poker.core.network.ApiResponse
import com.ghn.poker.feature.cards.data.sources.remote.EvaluatorRemoteDataSource
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class HandEvaluatorRepositoryImplTest {

    @Test
    fun evaluate_delegates_to_remote_data_source_and_returns_success() = runTest {
        val remoteDataSource = mock<EvaluatorRemoteDataSource>()
        val repository = HandEvaluatorRepositoryImpl(remoteDataSource)
        val heroCards = listOf(
            Card(CardSuit.SPADES, "A", 14),
            Card(CardSuit.HEARTS, "K", 13)
        )
        val boardCards = listOf(
            Card(CardSuit.DIAMONDS, "Q", 12),
            Card(CardSuit.CLUBS, "J", 11),
            Card(CardSuit.HEARTS, "10", 10)
        )
        val villainCards = listOf(
            Card(CardSuit.CLUBS, "9", 9),
            Card(CardSuit.DIAMONDS, "8", 8)
        )
        val simulationCount = 10000
        val expectedResponse = EvaluatorResponse(
            heroResult = 6000,
            villainResult = 3500,
            tiedResult = 500
        )

        everySuspend {
            remoteDataSource.evaluate(heroCards, boardCards, villainCards, simulationCount)
        } returns ApiResponse.Success(expectedResponse)

        val result = repository.evaluate(heroCards, boardCards, villainCards, simulationCount)

        result shouldBe ApiResponse.Success(expectedResponse)
        verifySuspend { remoteDataSource.evaluate(heroCards, boardCards, villainCards, simulationCount) }
    }

    @Test
    fun evaluate_returns_error_when_remote_data_source_fails() = runTest {
        val remoteDataSource = mock<EvaluatorRemoteDataSource>()
        val repository = HandEvaluatorRepositoryImpl(remoteDataSource)
        val heroCards = listOf(Card(CardSuit.SPADES, "A", 14), Card(CardSuit.HEARTS, "K", 13))
        val boardCards = emptyList<Card>()
        val villainCards = listOf(Card(CardSuit.CLUBS, "Q", 12), Card(CardSuit.DIAMONDS, "J", 11))
        val simulationCount = 10000

        everySuspend {
            remoteDataSource.evaluate(heroCards, boardCards, villainCards, simulationCount)
        } returns ApiResponse.Error.NetworkError

        val result = repository.evaluate(heroCards, boardCards, villainCards, simulationCount)

        result shouldBe ApiResponse.Error.NetworkError
        verifySuspend { remoteDataSource.evaluate(heroCards, boardCards, villainCards, simulationCount) }
    }

    @Test
    fun evaluate_returns_http_error_from_remote_data_source() = runTest {
        val remoteDataSource = mock<EvaluatorRemoteDataSource>()
        val repository = HandEvaluatorRepositoryImpl(remoteDataSource)
        val heroCards = listOf(Card(CardSuit.SPADES, "A", 14), Card(CardSuit.HEARTS, "K", 13))
        val boardCards = emptyList<Card>()
        val villainCards = listOf(Card(CardSuit.CLUBS, "Q", 12), Card(CardSuit.DIAMONDS, "J", 11))
        val simulationCount = 10000
        val httpError = ApiResponse.Error.HttpError(500, Exception("Server Error"))

        everySuspend {
            remoteDataSource.evaluate(heroCards, boardCards, villainCards, simulationCount)
        } returns httpError

        val result = repository.evaluate(heroCards, boardCards, villainCards, simulationCount)

        result shouldBe httpError
    }

    @Test
    fun getFiveCardRank_delegates_to_remote_data_source_and_returns_success() = runTest {
        val remoteDataSource = mock<EvaluatorRemoteDataSource>()
        val repository = HandEvaluatorRepositoryImpl(remoteDataSource)
        val heroCards = listOf(
            Card(CardSuit.SPADES, "A", 14),
            Card(CardSuit.SPADES, "K", 13),
            Card(CardSuit.SPADES, "Q", 12),
            Card(CardSuit.SPADES, "J", 11),
            Card(CardSuit.SPADES, "10", 10)
        )
        val expectedRank: Short = 1 // Royal Flush

        everySuspend { remoteDataSource.getFiveCardRank(heroCards) } returns ApiResponse.Success(expectedRank)

        val result = repository.getFiveCardRank(heroCards)

        result shouldBe ApiResponse.Success(expectedRank)
        verifySuspend { remoteDataSource.getFiveCardRank(heroCards) }
    }

    @Test
    fun getFiveCardRank_returns_error_when_remote_data_source_fails() = runTest {
        val remoteDataSource = mock<EvaluatorRemoteDataSource>()
        val repository = HandEvaluatorRepositoryImpl(remoteDataSource)
        val heroCards = listOf(
            Card(CardSuit.SPADES, "A", 14),
            Card(CardSuit.HEARTS, "K", 13),
            Card(CardSuit.DIAMONDS, "Q", 12),
            Card(CardSuit.CLUBS, "J", 11),
            Card(CardSuit.SPADES, "9", 9)
        )

        everySuspend { remoteDataSource.getFiveCardRank(heroCards) } returns ApiResponse.Error.NetworkError

        val result = repository.getFiveCardRank(heroCards)

        result shouldBe ApiResponse.Error.NetworkError
        verifySuspend { remoteDataSource.getFiveCardRank(heroCards) }
    }

    @Test
    fun evaluate_with_empty_board_cards() = runTest {
        val remoteDataSource = mock<EvaluatorRemoteDataSource>()
        val repository = HandEvaluatorRepositoryImpl(remoteDataSource)
        val heroCards = listOf(Card(CardSuit.SPADES, "A", 14), Card(CardSuit.HEARTS, "K", 13))
        val boardCards = emptyList<Card>()
        val villainCards = listOf(Card(CardSuit.CLUBS, "Q", 12), Card(CardSuit.DIAMONDS, "J", 11))
        val simulationCount = 50000
        val expectedResponse = EvaluatorResponse(
            heroResult = 25000,
            villainResult = 20000,
            tiedResult = 5000
        )

        everySuspend {
            remoteDataSource.evaluate(heroCards, boardCards, villainCards, simulationCount)
        } returns ApiResponse.Success(expectedResponse)

        val result = repository.evaluate(heroCards, boardCards, villainCards, simulationCount)

        result shouldBe ApiResponse.Success(expectedResponse)
    }
}
