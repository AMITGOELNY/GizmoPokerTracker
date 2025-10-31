package com.ghn.repository

import com.ghn.gizmodb.common.models.UserDTO
import com.ghn.gizmodb.tables.references.REFRESH_TOKEN
import com.ghn.gizmodb.tables.references.USER
import com.ghn.model.User
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldHaveLength
import io.kotest.matchers.string.shouldNotBeEmpty
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.time.Duration.Companion.days

@DisplayName("RefreshTokenRepository Integration Tests")
class RefreshTokenRepositoryImplTest : RepositoryTestBase() {

    private lateinit var repository: RefreshTokenRepositoryImpl
    private lateinit var userRepository: UserRepositoryImpl

    @BeforeEach
    fun setup() {
        repository = RefreshTokenRepositoryImpl(db)
        userRepository = UserRepositoryImpl(db, repository)
        clearAllTables()
    }

    private fun createTestUser(username: String = "testuser", password: String = "password123"): UserDTO {
        val user = User(null, username, password)
        userRepository.create(user)

        // Fetch the created user
        return db.fetchOne(USER, USER.USERNAME.eq(username))!!.into(UserDTO::class.java)
    }

    @Test
    @DisplayName("should generate refresh token and store hash in database")
    fun `generateRefreshToken creates token and stores in database`() {
        // Given
        val user = createTestUser()
        val userId = user.id!!

        // When
        val token = repository.generateRefreshToken(userId)

        // Then
        token.shouldNotBeEmpty()
        token.shouldHaveLength(36) // UUID format

        // Verify token is stored in database (as hash, not plain text)
        val records = db.selectFrom(REFRESH_TOKEN)
            .where(REFRESH_TOKEN.USER_ID.eq(userId))
            .fetch()

        records.size shouldBe 1
        val record = records.first()

        // Verify hash is different from plain token
        record.tokenHash shouldNotBe token
        record.tokenHash.shouldNotBeEmpty()

        // Verify expiry is set to 30 days from now (with some tolerance)
        val now = kotlin.time.Clock.System.now()
        val expectedExpiry = now + 30.days
        val actualExpiry = record.expiresAt

        actualExpiry.shouldNotBeNull()
        // Allow 1 second tolerance for test execution time
        val diff = (actualExpiry - expectedExpiry).inWholeSeconds
        (diff >= -1 && diff <= 1) shouldBe true

        // Verify created_at is set
        record.createdAt.shouldNotBeNull()
    }

    @Test
    @DisplayName("should validate token and return user for valid token")
    fun `validateAndGetUser with valid token returns UserDTO`() {
        // Given
        val user = createTestUser()
        val userId = user.id!!
        val token = repository.generateRefreshToken(userId)

        // When
        val result = repository.validateAndGetUser(token)

        // Then
        result.shouldBeInstanceOf<ApiCallResult.Success<UserDTO>>()
        val returnedUser = result.data
        returnedUser.id shouldBe userId
        returnedUser.username shouldBe user.username
    }

    @Test
    @DisplayName("should return NotFound for expired token")
    fun `validateAndGetUser with expired token returns NotFound`() {
        // Given
        val user = createTestUser()
        val userId = user.id!!
        val token = repository.generateRefreshToken(userId)

        // Manually update the token to be expired
        val expiredTime = kotlin.time.Clock.System.now() - 1.days
        db.update(REFRESH_TOKEN)
            .set(REFRESH_TOKEN.EXPIRES_AT, expiredTime)
            .where(REFRESH_TOKEN.USER_ID.eq(userId))
            .execute()

        // When
        val result = repository.validateAndGetUser(token)

        // Then
        result shouldBe ApiCallResult.NotFound
    }

    @Test
    @DisplayName("should return NotFound for non-existent token")
    fun `validateAndGetUser with non-existent token returns NotFound`() {
        // Given
        val nonExistentToken = "00000000-0000-0000-0000-000000000000"

        // When
        val result = repository.validateAndGetUser(nonExistentToken)

        // Then
        result shouldBe ApiCallResult.NotFound
    }

    @Test
    @DisplayName("should return NotFound for token after user is deleted")
    fun `validateAndGetUser with token for deleted user returns NotFound`() {
        // Given
        val user = createTestUser()
        val userId = user.id!!
        val token = repository.generateRefreshToken(userId)

        // Delete the user (cascade should delete refresh token)
        db.deleteFrom(USER).where(USER.ID.eq(userId)).execute()

        // When
        val result = repository.validateAndGetUser(token)

        // Then
        result shouldBe ApiCallResult.NotFound
    }

    @Test
    @DisplayName("should revoke token and remove from database")
    fun `revokeToken deletes token from database`() {
        // Given
        val user = createTestUser()
        val userId = user.id!!
        val token = repository.generateRefreshToken(userId)

        // Verify token exists
        repository.validateAndGetUser(token).shouldBeInstanceOf<ApiCallResult.Success<UserDTO>>()

        // When
        val result = repository.revokeToken(token)

        // Then
        result.shouldBeInstanceOf<ApiCallResult.Success<Unit>>()

        // Verify token is no longer valid
        val validateResult = repository.validateAndGetUser(token)
        validateResult shouldBe ApiCallResult.NotFound

        // Verify token is not in database
        val count = db.selectCount()
            .from(REFRESH_TOKEN)
            .where(REFRESH_TOKEN.USER_ID.eq(userId))
            .fetchOne(0, Int::class.java)
        count shouldBe 0
    }

    @Test
    @DisplayName("should revoke all tokens for a user")
    fun `revokeAllUserTokens deletes all tokens for user`() {
        // Given
        val user1 = createTestUser("user1")
        val user2 = createTestUser("user2")
        val userId1 = user1.id!!
        val userId2 = user2.id!!

        // Create multiple tokens for user1
        val token1 = repository.generateRefreshToken(userId1)
        val token2 = repository.generateRefreshToken(userId1)
        val token3 = repository.generateRefreshToken(userId1)

        // Create tokens for user2
        val token4 = repository.generateRefreshToken(userId2)

        // When
        val result = repository.revokeAllUserTokens(userId1)

        // Then
        result.shouldBeInstanceOf<ApiCallResult.Success<Unit>>()

        // Verify all user1 tokens are revoked
        repository.validateAndGetUser(token1) shouldBe ApiCallResult.NotFound
        repository.validateAndGetUser(token2) shouldBe ApiCallResult.NotFound
        repository.validateAndGetUser(token3) shouldBe ApiCallResult.NotFound

        // Verify user2 tokens still exist
        repository.validateAndGetUser(token4).shouldBeInstanceOf<ApiCallResult.Success<UserDTO>>()

        // Verify database state
        val user1TokenCount = db.selectCount()
            .from(REFRESH_TOKEN)
            .where(REFRESH_TOKEN.USER_ID.eq(userId1))
            .fetchOne(0, Int::class.java)
        user1TokenCount shouldBe 0

        val user2TokenCount = db.selectCount()
            .from(REFRESH_TOKEN)
            .where(REFRESH_TOKEN.USER_ID.eq(userId2))
            .fetchOne(0, Int::class.java)
        user2TokenCount shouldBe 1
    }

    @Test
    @DisplayName("should generate unique tokens for same user")
    fun `generateRefreshToken creates unique tokens`() {
        // Given
        val user = createTestUser()
        val userId = user.id!!

        // When
        val token1 = repository.generateRefreshToken(userId)
        val token2 = repository.generateRefreshToken(userId)
        val token3 = repository.generateRefreshToken(userId)

        // Then
        token1 shouldNotBe token2
        token2 shouldNotBe token3
        token1 shouldNotBe token3

        // Verify all tokens are valid
        repository.validateAndGetUser(token1).shouldBeInstanceOf<ApiCallResult.Success<UserDTO>>()
        repository.validateAndGetUser(token2).shouldBeInstanceOf<ApiCallResult.Success<UserDTO>>()
        repository.validateAndGetUser(token3).shouldBeInstanceOf<ApiCallResult.Success<UserDTO>>()
    }

    @Test
    @DisplayName("should return NotFound when revoking non-existent token")
    fun `revokeToken with non-existent token returns NotFound`() {
        // Given
        val nonExistentToken = "00000000-0000-0000-0000-000000000000"

        // When
        val result = repository.revokeToken(nonExistentToken)

        // Then
        result shouldBe ApiCallResult.NotFound
    }
}
