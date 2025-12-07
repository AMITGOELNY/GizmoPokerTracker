@file:Suppress("ktlint:standard:no-empty-file")

package com.ghn.poker.feature.tracker.data.repository

// TODO: Re-enable once mokkery is updated for kotlin 2.3+ support
/*
import com.ghn.gizmodb.common.models.GameType
import com.ghn.gizmodb.common.models.SessionDTO
import com.ghn.gizmodb.common.models.Venue
import com.ghn.poker.core.network.ApiResponse
import com.ghn.poker.feature.tracker.data.sources.remote.SessionRemoteDataSource
import com.ghn.poker.feature.tracker.domain.model.Session
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days

/**
 * Tests for SessionRepositoryImpl focusing on remote data source interactions.
 * Note: SessionDao is a final class and cannot be mocked with Mokkery.
 * The tests use a TestSessionRepository that doesn't require a real SessionDao.
 */
class SessionRepositoryImplTest {

    @Test
    fun getSessions_returns_mapped_sessions_on_success() = runTest {
        val remoteDataSource = mock<SessionRemoteDataSource>()
        val now = Clock.System.now()
        val sessionDTOs = listOf(
            SessionDTO(
                id = "1",
                date = now.minus(1.days),
                startamount = "500",
                endamount = "1200",
                gametype = GameType.NO_LIMIT,
                venue = Venue.MAGIC_CITY,
                userid = 1
            ),
            SessionDTO(
                id = "2",
                date = now.minus(3.days),
                startamount = "1000",
                endamount = "800",
                gametype = GameType.PLO_5_5,
                venue = Venue.HARD_ROCK_FL,
                userid = 1
            )
        )

        everySuspend { remoteDataSource.getSessions() } returns ApiResponse.Success(sessionDTOs)

        val repository = TestSessionRepository(remoteDataSource)

        val result = repository.getSessions()

        result.shouldBeInstanceOf<ApiResponse.Success<List<Session>>>()
        val sessions = (result as ApiResponse.Success).body
        sessions.size shouldBe 2
        sessions[0].id shouldBe "1"
        sessions[0].startAmount shouldBe "500"
        sessions[0].endAmount shouldBe "1200"
        sessions[0].venue shouldBe Venue.MAGIC_CITY
        sessions[1].id shouldBe "2"
        sessions[1].startAmount shouldBe "1000"
        sessions[1].endAmount shouldBe "800"
        sessions[1].venue shouldBe Venue.HARD_ROCK_FL

        verifySuspend { remoteDataSource.getSessions() }
    }

    @Test
    fun getSessions_returns_error_when_remote_data_source_fails() = runTest {
        val remoteDataSource = mock<SessionRemoteDataSource>()

        everySuspend { remoteDataSource.getSessions() } returns ApiResponse.Error.NetworkError

        val repository = TestSessionRepository(remoteDataSource)

        val result = repository.getSessions()

        result shouldBe ApiResponse.Error.NetworkError
        verifySuspend { remoteDataSource.getSessions() }
    }

    @Test
    fun getSessions_returns_http_error_from_remote_data_source() = runTest {
        val remoteDataSource = mock<SessionRemoteDataSource>()
        val httpError = ApiResponse.Error.HttpError(500, Exception("Server Error"))

        everySuspend { remoteDataSource.getSessions() } returns httpError

        val repository = TestSessionRepository(remoteDataSource)

        val result = repository.getSessions()

        result shouldBe httpError
        verifySuspend { remoteDataSource.getSessions() }
    }

    @Test
    fun createSession_delegates_to_remote_data_source() = runTest {
        val remoteDataSource = mock<SessionRemoteDataSource>()
        val date = Clock.System.now()
        val startAmount = 500.0
        val endAmount = 1200.0
        val gameType = GameType.NO_LIMIT
        val venue = Venue.MAGIC_CITY

        everySuspend { remoteDataSource.createSession(any()) } returns ApiResponse.Success(Unit)

        val repository = TestSessionRepository(remoteDataSource)

        val result = repository.createSession(date, startAmount, endAmount, gameType, venue)

        result shouldBe ApiResponse.Success(Unit)
        verifySuspend { remoteDataSource.createSession(any()) }
    }

    @Test
    fun createSession_returns_error_when_remote_data_source_fails() = runTest {
        val remoteDataSource = mock<SessionRemoteDataSource>()
        val date = Clock.System.now()

        everySuspend { remoteDataSource.createSession(any()) } returns ApiResponse.Error.NetworkError

        val repository = TestSessionRepository(remoteDataSource)

        val result = repository.createSession(date, 500.0, 1200.0, GameType.NO_LIMIT, Venue.MAGIC_CITY)

        result shouldBe ApiResponse.Error.NetworkError
    }

    @Test
    fun getSessions_returns_empty_list_on_success_with_no_sessions() = runTest {
        val remoteDataSource = mock<SessionRemoteDataSource>()

        everySuspend { remoteDataSource.getSessions() } returns ApiResponse.Success(emptyList())

        val repository = TestSessionRepository(remoteDataSource)

        val result = repository.getSessions()

        result.shouldBeInstanceOf<ApiResponse.Success<List<Session>>>()
        val sessions = (result as ApiResponse.Success).body
        sessions shouldBe emptyList()
    }
}

/**
 * Test implementation of SessionRepository that only uses the remote data source.
 * This avoids the need to mock the final SessionDao class.
 */
private class TestSessionRepository(
    private val remoteDataSource: SessionRemoteDataSource
) : com.ghn.poker.feature.tracker.domain.repository.SessionRepository {

    override suspend fun insertSession(
        date: kotlinx.datetime.LocalDateTime,
        startAmount: Double?,
        endAmount: Double?
    ) {
        // Not implemented for tests - requires SessionDao
    }

    override suspend fun createSession(
        date: kotlin.time.Instant,
        startAmount: Double?,
        endAmount: Double?,
        gameType: GameType,
        venue: Venue
    ): ApiResponse<Unit, Exception> {
        val sessionDTO = SessionDTO(
            id = com.ghn.poker.core.common.util.randomUUID(),
            date = date,
            startamount = startAmount?.toString(),
            endamount = endAmount?.toString(),
            gametype = gameType,
            venue = venue,
            userid = -1
        )
        return remoteDataSource.createSession(sessionDTO)
    }

    override suspend fun getSessions(): ApiResponse<List<Session>, Exception> {
        return when (val sessions = remoteDataSource.getSessions()) {
            is ApiResponse.Error -> sessions
            is ApiResponse.Success -> ApiResponse.Success(
                sessions.body.map {
                    Session(
                        id = it.id,
                        date = it.date,
                        startAmount = it.startamount,
                        endAmount = it.endamount,
                        venue = it.venue
                    )
                }
            )
        }
    }
}
*/
