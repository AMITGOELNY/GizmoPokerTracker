package com.ghn.service

import com.ghn.gizmodb.common.models.GameType
import com.ghn.gizmodb.common.models.SessionDTO
import com.ghn.gizmodb.common.models.Venue
import com.ghn.repository.SessionRepository
import com.ghn.repository.SessionResponse
import io.kotest.matchers.shouldBe
import io.ktor.http.HttpStatusCode
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.time.Duration.Companion.hours

@DisplayName("SessionServiceImpl")
class SessionServiceImplTest {
    private lateinit var repository: SessionRepository
    private lateinit var service: SessionServiceImpl

    private val testSession = SessionDTO(
        id = "session-1",
        date = kotlin.time.Clock.System.now(),
        startamount = "100.00",
        endamount = "150.00",
        gametype = GameType.PLO_2_2,
        venue = Venue.HARD_ROCK_FL,
        userid = 1
    )

    private val testSession2 = SessionDTO(
        id = "session-2",
        date = kotlin.time.Clock.System.now() - 24.hours,
        startamount = "200.00",
        endamount = "180.00",
        gametype = GameType.PLO_2_2,
        venue = Venue.HIALEAH_PARK,
        userid = 1
    )

    @BeforeEach
    fun setup() {
        repository = mockk()
        service = SessionServiceImpl(repository)
    }

    @Nested
    @DisplayName("getUserSessions")
    inner class GetUserSessions {
        @Test
        fun `should return list of sessions when repository returns sessions`() {
            val userId = 1
            val sessions = listOf(testSession, testSession2)
            every { repository.getUserSessions(userId) } returns sessions

            val result = service.getUserSessions(userId)

            result shouldBe sessions
            verify(exactly = 1) { repository.getUserSessions(userId) }
        }

        @Test
        fun `should return empty list when user has no sessions`() {
            val userId = 999
            every { repository.getUserSessions(userId) } returns emptyList()

            val result = service.getUserSessions(userId)

            result shouldBe emptyList()
            verify(exactly = 1) { repository.getUserSessions(userId) }
        }

        @Test
        fun `should return single session when user has one session`() {
            val userId = 1
            val sessions = listOf(testSession)
            every { repository.getUserSessions(userId) } returns sessions

            val result = service.getUserSessions(userId)

            result shouldBe sessions
            verify(exactly = 1) { repository.getUserSessions(userId) }
        }
    }

    @Nested
    @DisplayName("getUserSessionById")
    inner class GetUserSessionById {
        @Test
        fun `should return success when session exists`() {
            val sessionId = "session-1"
            val userId = 1
            val response = SessionResponse.Success(testSession)
            every { repository.getUserSessionById(sessionId, userId) } returns response

            val result = service.getUserSessionById(sessionId, userId)

            result shouldBe response
            verify(exactly = 1) { repository.getUserSessionById(sessionId, userId) }
        }

        @Test
        fun `should return HttpError when session not found`() {
            val sessionId = "nonexistent"
            val userId = 1
            val response = SessionResponse.Error.HttpError(HttpStatusCode.NotFound)
            every { repository.getUserSessionById(sessionId, userId) } returns response

            val result = service.getUserSessionById(sessionId, userId)

            result shouldBe response
            verify(exactly = 1) { repository.getUserSessionById(sessionId, userId) }
        }

        @Test
        fun `should return HttpError for unauthorized access`() {
            val sessionId = "session-1"
            val userId = 999
            val response = SessionResponse.Error.HttpError(HttpStatusCode.Forbidden)
            every { repository.getUserSessionById(sessionId, userId) } returns response

            val result = service.getUserSessionById(sessionId, userId)

            result shouldBe response
            verify(exactly = 1) { repository.getUserSessionById(sessionId, userId) }
        }

        @Test
        fun `should return NetworkError when network fails`() {
            val sessionId = "session-1"
            val userId = 1
            val response = SessionResponse.Error.NetworkError
            every { repository.getUserSessionById(sessionId, userId) } returns response

            val result = service.getUserSessionById(sessionId, userId)

            result shouldBe response
            verify(exactly = 1) { repository.getUserSessionById(sessionId, userId) }
        }
    }

    @Nested
    @DisplayName("createSession")
    inner class CreateSession {
        @Test
        fun `should delegate to repository for valid session creation`() {
            val userId = 1
            every { repository.createSession(testSession, userId) } returns Unit

            service.createSession(testSession, userId)

            verify(exactly = 1) { repository.createSession(testSession, userId) }
        }

        @Test
        fun `should create session with different game types`() {
            val session = testSession.copy(gametype = GameType.PLO_2_2)
            val userId = 1
            every { repository.createSession(session, userId) } returns Unit

            service.createSession(session, userId)

            verify(exactly = 1) { repository.createSession(session, userId) }
        }

        @Test
        fun `should create session with different venues`() {
            val session = testSession.copy(venue = Venue.HIALEAH_PARK)
            val userId = 1
            every { repository.createSession(session, userId) } returns Unit

            service.createSession(session, userId)

            verify(exactly = 1) { repository.createSession(session, userId) }
        }

        @Test
        fun `should create session with null amounts`() {
            val session = testSession.copy(startamount = null, endamount = null)
            val userId = 1
            every { repository.createSession(session, userId) } returns Unit

            service.createSession(session, userId)

            verify(exactly = 1) { repository.createSession(session, userId) }
        }
    }

    @Nested
    @DisplayName("deleteSession")
    inner class DeleteSession {
        @Test
        fun `should return OK when session deleted successfully`() {
            val sessionId = "session-1"
            val userId = 1
            every { repository.deleteSession(sessionId, userId) } returns HttpStatusCode.OK

            val result = service.deleteSession(sessionId, userId)

            result shouldBe HttpStatusCode.OK
            verify(exactly = 1) { repository.deleteSession(sessionId, userId) }
        }

        @Test
        fun `should return NotFound when session does not exist`() {
            val sessionId = "nonexistent"
            val userId = 1
            every { repository.deleteSession(sessionId, userId) } returns HttpStatusCode.NotFound

            val result = service.deleteSession(sessionId, userId)

            result shouldBe HttpStatusCode.NotFound
            verify(exactly = 1) { repository.deleteSession(sessionId, userId) }
        }

        @Test
        fun `should return Forbidden when user not authorized to delete`() {
            val sessionId = "session-1"
            val userId = 999
            every { repository.deleteSession(sessionId, userId) } returns HttpStatusCode.Forbidden

            val result = service.deleteSession(sessionId, userId)

            result shouldBe HttpStatusCode.Forbidden
            verify(exactly = 1) { repository.deleteSession(sessionId, userId) }
        }

        @Test
        fun `should return InternalServerError on repository failure`() {
            val sessionId = "session-1"
            val userId = 1
            every { repository.deleteSession(sessionId, userId) } returns HttpStatusCode.InternalServerError

            val result = service.deleteSession(sessionId, userId)

            result shouldBe HttpStatusCode.InternalServerError
            verify(exactly = 1) { repository.deleteSession(sessionId, userId) }
        }
    }
}
