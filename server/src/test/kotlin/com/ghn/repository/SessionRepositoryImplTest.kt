package com.ghn.repository

import com.ghn.gizmodb.common.models.GameType
import com.ghn.gizmodb.common.models.SessionDTO
import com.ghn.gizmodb.common.models.Venue
import com.ghn.model.User
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.ktor.http.HttpStatusCode
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("SessionRepository Integration Tests")
class SessionRepositoryImplTest : RepositoryTestBase() {

    private lateinit var repository: SessionRepositoryImpl
    private lateinit var userRepository: UserRepositoryImpl
    private var testUserId: Int = 0

    @BeforeEach
    fun setup() {
        repository = SessionRepositoryImpl(db)
        userRepository = UserRepositoryImpl(db)
        clearAllTables()

        // Create a test user for session tests
        val user = User(null, "testuser", "password")
        userRepository.create(user)

        // Get the user ID by querying the database
        testUserId = db.fetchOne("SELECT id FROM user WHERE username = ?", "testuser")
            ?.getValue(0, Int::class.java) ?: 1
    }

    private fun createTestSession(
        id: String = "session-${System.currentTimeMillis()}",
        userId: Int = testUserId,
        startAmount: String = "100.00",
        endAmount: String = "150.00"
    ) = SessionDTO(
        id = id,
        date = kotlin.time.Clock.System.now(),
        startamount = startAmount,
        endamount = endAmount,
        gametype = GameType.NO_LIMIT,
        venue = Venue.OTHER,
        userid = userId
    )

    @Test
    @DisplayName("should create session successfully")
    fun `test createSession`() {
        // Given
        val session = createTestSession()

        // When
        repository.createSession(session, testUserId)

        // Then - verify session was created by fetching it
        val sessions = repository.getUserSessions(testUserId)
        sessions shouldHaveSize 1
        sessions[0].id shouldBe session.id
    }

    @Test
    @DisplayName("should return all user sessions")
    fun `test getUserSessions`() {
        // Given
        val session1 = createTestSession(id = "session-1", startAmount = "100", endAmount = "150")
        val session2 = createTestSession(id = "session-2", startAmount = "200", endAmount = "250")
        val session3 = createTestSession(id = "session-3", startAmount = "300", endAmount = "350")

        repository.createSession(session1, testUserId)
        repository.createSession(session2, testUserId)
        repository.createSession(session3, testUserId)

        // When
        val result = repository.getUserSessions(testUserId)

        // Then
        result shouldHaveSize 3
        result.map { it.id }.toSet() shouldBe setOf("session-1", "session-2", "session-3")
    }

    @Test
    @DisplayName("should return empty list when user has no sessions")
    fun `test getUserSessions with no sessions`() {
        // Given - no sessions created

        // When
        val result = repository.getUserSessions(testUserId)

        // Then
        result shouldHaveSize 0
    }

    @Test
    @DisplayName("should return Success with session when session exists")
    fun `test getUserSessionById success`() {
        // Given
        val sessionId = "session-123"
        val session = createTestSession(id = sessionId)
        repository.createSession(session, testUserId)

        // When
        val result = repository.getUserSessionById(sessionId, testUserId)

        // Then
        result.shouldBeInstanceOf<SessionResponse.Success<SessionDTO>>()
        (result as SessionResponse.Success).body.id shouldBe sessionId
    }

    @Test
    @DisplayName("should return HttpError when session does not exist")
    fun `test getUserSessionById not found`() {
        // Given
        val sessionId = "non-existent"

        // When
        val result = repository.getUserSessionById(sessionId, testUserId)

        // Then
        result.shouldBeInstanceOf<SessionResponse.Error.HttpError>()
        (result as SessionResponse.Error.HttpError).code shouldBe HttpStatusCode.BadRequest
    }

    @Test
    @DisplayName("should return HttpError when user ID does not match")
    fun `test getUserSessionById wrong user`() {
        // Given
        val sessionId = "session-123"
        val session = createTestSession(id = sessionId)
        repository.createSession(session, testUserId)

        // When - try to fetch with different user ID
        val result = repository.getUserSessionById(sessionId, 999)

        // Then
        result.shouldBeInstanceOf<SessionResponse.Error.HttpError>()
        (result as SessionResponse.Error.HttpError).code shouldBe HttpStatusCode.BadRequest
    }

    @Test
    @DisplayName("should delete session and return OK when successful")
    fun `test deleteSession success`() {
        // Given
        val sessionId = "session-to-delete"
        val session = createTestSession(id = sessionId)
        repository.createSession(session, testUserId)

        // Verify session exists
        repository.getUserSessions(testUserId) shouldHaveSize 1

        // When
        val result = repository.deleteSession(sessionId, testUserId)

        // Then
        result shouldBe HttpStatusCode.OK

        // Verify session was deleted
        repository.getUserSessions(testUserId) shouldHaveSize 0
    }

    @Test
    @DisplayName("should return NotAcceptable when session does not exist")
    fun `test deleteSession not found`() {
        // Given
        val sessionId = "non-existent"

        // When
        val result = repository.deleteSession(sessionId, testUserId)

        // Then
        result shouldBe HttpStatusCode.NotAcceptable
    }

    @Test
    @DisplayName("should not delete session when user ID does not match")
    fun `test deleteSession wrong user`() {
        // Given
        val sessionId = "session-protected"
        val session = createTestSession(id = sessionId)
        repository.createSession(session, testUserId)

        // When - try to delete with different user ID
        val result = repository.deleteSession(sessionId, 999)

        // Then
        result shouldBe HttpStatusCode.NotAcceptable

        // Verify session still exists
        repository.getUserSessions(testUserId) shouldHaveSize 1
    }

    @Test
    @DisplayName("should only return sessions for the specified user")
    fun `test getUserSessions filters by user ID`() {
        // Given - create another user
        val user2 = User(null, "user2", "password")
        userRepository.create(user2)
        val user2Id = db.fetchOne("SELECT id FROM user WHERE username = ?", "user2")
            ?.getValue(0, Int::class.java) ?: 2

        // Create sessions for both users
        repository.createSession(createTestSession(id = "user1-session"), testUserId)
        repository.createSession(createTestSession(id = "user2-session"), user2Id)

        // When
        val user1Sessions = repository.getUserSessions(testUserId)
        val user2Sessions = repository.getUserSessions(user2Id)

        // Then
        user1Sessions shouldHaveSize 1
        user1Sessions[0].id shouldBe "user1-session"

        user2Sessions shouldHaveSize 1
        user2Sessions[0].id shouldBe "user2-session"
    }
}
