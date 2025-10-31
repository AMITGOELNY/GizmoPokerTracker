package com.ghn.repository

import com.ghn.model.TokenResponse
import com.ghn.model.User
import com.ghn.plugins.JwtConfig
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldNotBeEmpty
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("UserRepository Integration Tests")
class UserRepositoryImplTest : RepositoryTestBase() {

    private lateinit var repository: UserRepositoryImpl
    private lateinit var refreshTokenRepository: RefreshTokenRepository

    @BeforeEach
    fun setup() {
        refreshTokenRepository = RefreshTokenRepositoryImpl(db)
        repository = UserRepositoryImpl(db, refreshTokenRepository)
        JwtConfig.initialize("test-secret-key")
        clearAllTables()
    }

    @Test
    @DisplayName("should create user successfully")
    fun `test create user`() {
        // Given
        val user = User(
            id = null,
            username = "newuser",
            password = "password123"
        )

        // When
        val result = repository.create(user)

        // Then
        result.shouldBeInstanceOf<ApiCallResult.Success<Unit>>()

        // Verify user was created by attempting to login
        val loginResult = repository.login("newuser", "password123")
        loginResult.shouldBeInstanceOf<ApiCallResult.Success<TokenResponse>>()
    }

    @Test
    @DisplayName("should return Success with TokenResponse when login credentials are valid")
    fun `test successful login`() {
        // Given
        val username = "testuser"
        val password = "password123"
        val user = User(
            id = null,
            username = username,
            password = password
        )
        repository.create(user)

        // When
        val result = repository.login(username, password)

        // Then
        result.shouldBeInstanceOf<ApiCallResult.Success<TokenResponse>>()
        val tokenResponse = result.data
        tokenResponse.accessToken.shouldNotBeEmpty()
        tokenResponse.refreshToken.shouldNotBeEmpty()
        tokenResponse.accessToken shouldNotBe tokenResponse.refreshToken
    }

    @Test
    @DisplayName("should return BadPassword when password is incorrect")
    fun `test login with incorrect password`() {
        // Given
        val username = "testuser"
        val correctPassword = "correctpassword"
        val wrongPassword = "wrongpassword"
        val user = User(
            id = null,
            username = username,
            password = correctPassword
        )
        repository.create(user)

        // When
        val result = repository.login(username, wrongPassword)

        // Then
        result shouldBe ApiCallResult.BadPassword
    }

    @Test
    @DisplayName("should return NotFound when user does not exist")
    fun `test login with non-existent user`() {
        // Given
        val username = "nonexistent"
        val password = "password123"

        // When
        val result = repository.login(username, password)

        // Then
        result shouldBe ApiCallResult.NotFound
    }

    @Test
    @DisplayName("should create multiple users with unique usernames")
    fun `test create multiple users`() {
        // Given
        val user1 = User(null, "user1", "password1")
        val user2 = User(null, "user2", "password2")

        // When
        val result1 = repository.create(user1)
        val result2 = repository.create(user2)

        // Then
        result1.shouldBeInstanceOf<ApiCallResult.Success<Unit>>()
        result2.shouldBeInstanceOf<ApiCallResult.Success<Unit>>()

        // Verify both users can login
        val login1 = repository.login("user1", "password1")
        val login2 = repository.login("user2", "password2")
        login1.shouldBeInstanceOf<ApiCallResult.Success<TokenResponse>>()
        login2.shouldBeInstanceOf<ApiCallResult.Success<TokenResponse>>()

        // Verify tokens are different
        val tokenResponse1 = login1.data
        val tokenResponse2 = login2.data
        tokenResponse1.accessToken shouldNotBe tokenResponse2.accessToken
        tokenResponse1.refreshToken shouldNotBe tokenResponse2.refreshToken
    }

    @Test
    @DisplayName("should reject password that is too short")
    fun `test create user with password too short`() {
        // Given - password with only 7 characters (minimum is 8)
        val user = User(null, "testuser", "short12")

        // When & Then
        val exception = org.junit.jupiter.api.assertThrows<IllegalArgumentException> {
            repository.create(user)
        }
        exception.message shouldBe "Expected password to be in 8..30 but was 7"
    }

    @Test
    @DisplayName("should reject password that is too long")
    fun `test create user with password too long`() {
        // Given - password with 31 characters (maximum is 30)
        val user = User(null, "testuser", "a".repeat(31))

        // When & Then
        val exception = org.junit.jupiter.api.assertThrows<IllegalArgumentException> {
            repository.create(user)
        }
        exception.message shouldBe "Expected password to be in 8..30 but was 31"
    }

    @Test
    @DisplayName("should accept password at minimum length boundary")
    fun `test create user with password at minimum length`() {
        // Given - password with exactly 8 characters
        val user = User(null, "testuser", "12345678")

        // When
        val result = repository.create(user)

        // Then
        result.shouldBeInstanceOf<ApiCallResult.Success<Unit>>()
    }

    @Test
    @DisplayName("should accept password at maximum length boundary")
    fun `test create user with password at maximum length`() {
        // Given - password with exactly 30 characters
        val user = User(null, "testuser", "a".repeat(30))

        // When
        val result = repository.create(user)

        // Then
        result.shouldBeInstanceOf<ApiCallResult.Success<Unit>>()
    }
}
