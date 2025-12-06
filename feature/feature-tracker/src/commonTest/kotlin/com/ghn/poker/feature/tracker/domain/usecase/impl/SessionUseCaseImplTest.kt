package com.ghn.poker.feature.tracker.domain.usecase.impl

import com.ghn.gizmodb.common.models.GameType
import com.ghn.gizmodb.common.models.Venue
import com.ghn.poker.core.network.ApiResponse
import com.ghn.poker.feature.tracker.domain.model.Session
import com.ghn.poker.feature.tracker.domain.model.SessionData
import com.ghn.poker.feature.tracker.domain.repository.SessionRepository
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import kotlin.test.Test
import kotlin.time.Instant

class SessionUseCaseImplTest {

    @Test
    fun insertSession_delegates_to_repository() = runTest {
        val repository = mock<SessionRepository>()
        val useCase = SessionUseCaseImpl(repository)
        val date = LocalDateTime(2024, 5, 12, 14, 30)
        val startAmount = 120.0
        val endAmount = 250.0

        everySuspend { repository.insertSession(date, startAmount, endAmount) } returns Unit

        useCase.insertSession(date, startAmount, endAmount)

        verifySuspend { repository.insertSession(date, startAmount, endAmount) }
    }

    @Test
    fun createSession_delegates_to_repository_and_returns_result() = runTest {
        val repository = mock<SessionRepository>()
        val useCase = SessionUseCaseImpl(repository)
        val date = Instant.fromEpochMilliseconds(1_000)
        val startAmount = 100.0
        val endAmount = 180.0
        val gameType = GameType.PLO_5_5
        val venue = Venue.MAGIC_CITY
        val expected = ApiResponse.Success(Unit)

        everySuspend {
            repository.createSession(date, startAmount, endAmount, gameType, venue)
        } returns expected

        val result = useCase.createSession(date, startAmount, endAmount, gameType, venue)

        result shouldBe expected
        verifySuspend { repository.createSession(date, startAmount, endAmount, gameType, venue) }
    }

    @Test
    fun getSessions_maps_repository_response() = runTest {
        val repository = mock<SessionRepository>()
        val useCase = SessionUseCaseImpl(repository)
        val sessionOne = Session(
            id = "1",
            date = Instant.fromEpochMilliseconds(1_000),
            startAmount = "200",
            endAmount = "350",
            venue = Venue.HARD_ROCK_FL
        )
        val sessionTwo = Session(
            id = "2",
            date = Instant.fromEpochMilliseconds(2_000),
            startAmount = null,
            endAmount = null,
            venue = null
        )
        val expected = ApiResponse.Success(
            listOf(
                SessionData(
                    date = sessionOne.date,
                    startAmount = sessionOne.startAmount,
                    endAmount = sessionOne.endAmount,
                    venue = sessionOne.venue
                ),
                SessionData(
                    date = sessionTwo.date,
                    startAmount = sessionTwo.startAmount,
                    endAmount = sessionTwo.endAmount,
                    venue = sessionTwo.venue
                )
            )
        )

        everySuspend {
            repository.getSessions()
        } returns ApiResponse.Success(listOf(sessionOne, sessionTwo))

        val result = useCase.getSessions()

        result shouldBe expected
        verifySuspend { repository.getSessions() }
    }

    @Test
    fun getSessions_propagates_error() = runTest {
        val repository = mock<SessionRepository>()
        val useCase = SessionUseCaseImpl(repository)
        val error = ApiResponse.Error.NetworkError

        everySuspend { repository.getSessions() } returns error

        val result = useCase.getSessions()

        result shouldBe error
        verifySuspend { repository.getSessions() }
    }
}
