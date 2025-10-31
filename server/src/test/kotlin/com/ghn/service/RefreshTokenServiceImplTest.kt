package com.ghn.service

import com.ghn.gizmodb.common.models.UserDTO
import com.ghn.model.TokenResponse
import com.ghn.plugins.JwtConfig
import com.ghn.repository.ApiCallResult
import com.ghn.repository.RefreshTokenRepository
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldNotBeEmpty
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("RefreshTokenService Unit Tests")
class RefreshTokenServiceImplTest {

    private lateinit var repository: RefreshTokenRepository
    private lateinit var service: RefreshTokenService

    @BeforeEach
    fun setup() {
        repository = mockk()
        service = RefreshTokenServiceImpl(repository)
        JwtConfig.initialize("test-secret-key-for-jwt")
    }

    @Test
    @DisplayName("should return new token pair when refresh token is valid")
    fun `refresh with valid token returns new TokenResponse`() {
        // Given
        val oldRefreshToken = "old-refresh-token-uuid"
        val newRefreshToken = "new-refresh-token-uuid"
        val userDTO = UserDTO(
            id = 1,
            username = "testuser",
            password = "hashed-password",
            createdAt = kotlin.time.Clock.System.now(),
            updatedAt = kotlin.time.Clock.System.now()
        )

        every { repository.validateAndGetUser(oldRefreshToken) } returns ApiCallResult.Success(userDTO)
        every { repository.generateRefreshToken(1) } returns newRefreshToken
        every { repository.revokeToken(oldRefreshToken) } returns ApiCallResult.Success(Unit)

        // When
        val result = service.refresh(oldRefreshToken)

        // Then
        result.shouldBeInstanceOf<ApiCallResult.Success<TokenResponse>>()
        val tokenResponse = result.data

        tokenResponse.accessToken.shouldNotBeEmpty()
        tokenResponse.refreshToken shouldBe newRefreshToken
        tokenResponse.refreshToken shouldNotBe oldRefreshToken

        // Verify repository methods were called
        verify(exactly = 1) { repository.validateAndGetUser(oldRefreshToken) }
        verify(exactly = 1) { repository.generateRefreshToken(1) }
        verify(exactly = 1) { repository.revokeToken(oldRefreshToken) }
    }

    @Test
    @DisplayName("should return Unauthorized when refresh token is expired")
    fun `refresh with expired token returns Unauthorized`() {
        // Given
        val expiredToken = "expired-token-uuid"
        every { repository.validateAndGetUser(expiredToken) } returns ApiCallResult.NotFound

        // When
        val result = service.refresh(expiredToken)

        // Then
        result shouldBe ApiCallResult.Unauthorized

        // Verify no token generation or revocation occurred
        verify(exactly = 1) { repository.validateAndGetUser(expiredToken) }
        verify(exactly = 0) { repository.generateRefreshToken(any()) }
        verify(exactly = 0) { repository.revokeToken(any()) }
    }

    @Test
    @DisplayName("should return Unauthorized when refresh token is invalid")
    fun `refresh with invalid token returns Unauthorized`() {
        // Given
        val invalidToken = "invalid-token-uuid"
        every { repository.validateAndGetUser(invalidToken) } returns ApiCallResult.NotFound

        // When
        val result = service.refresh(invalidToken)

        // Then
        result shouldBe ApiCallResult.Unauthorized
        verify(exactly = 1) { repository.validateAndGetUser(invalidToken) }
    }

    @Test
    @DisplayName("should return Unauthorized when user is not authorized")
    fun `refresh with unauthorized result returns Unauthorized`() {
        // Given
        val token = "some-token"
        every { repository.validateAndGetUser(token) } returns ApiCallResult.Unauthorized

        // When
        val result = service.refresh(token)

        // Then
        result shouldBe ApiCallResult.Unauthorized
    }

    @Test
    @DisplayName("should return Failure when repository returns Failure")
    fun `refresh with repository failure returns Failure`() {
        // Given
        val token = "some-token"
        val failureReason = "Database connection failed"
        every { repository.validateAndGetUser(token) } returns ApiCallResult.Failure(failureReason)

        // When
        val result = service.refresh(token)

        // Then
        result.shouldBeInstanceOf<ApiCallResult.Failure>()
        result.reason shouldBe failureReason
    }

    @Test
    @DisplayName("should revoke old token after generating new one - token rotation")
    fun `refresh performs token rotation correctly`() {
        // Given
        val oldToken = "old-token"
        val newToken = "new-token"
        val userDTO = UserDTO(
            id = 2,
            username = "user2",
            password = "password",
            createdAt = kotlin.time.Clock.System.now(),
            updatedAt = kotlin.time.Clock.System.now()
        )

        every { repository.validateAndGetUser(oldToken) } returns ApiCallResult.Success(userDTO)
        every { repository.generateRefreshToken(2) } returns newToken
        every { repository.revokeToken(oldToken) } returns ApiCallResult.Success(Unit)

        // When
        val result = service.refresh(oldToken)

        // Then
        result.shouldBeInstanceOf<ApiCallResult.Success<TokenResponse>>()

        // Verify the order: validate -> generate -> revoke
        verify(exactly = 1) { repository.validateAndGetUser(oldToken) }
        verify(exactly = 1) { repository.generateRefreshToken(2) }
        verify(exactly = 1) { repository.revokeToken(oldToken) }
    }

    @Test
    @DisplayName("should generate different access tokens for different users")
    fun `refresh generates different access tokens for different users`() {
        // Given
        val token1 = "token1"
        val token2 = "token2"
        val user1 = UserDTO(
            id = 1,
            username = "user1",
            password = "pass1",
            createdAt = kotlin.time.Clock.System.now(),
            updatedAt = kotlin.time.Clock.System.now()
        )
        val user2 = UserDTO(
            id = 2,
            username = "user2",
            password = "pass2",
            createdAt = kotlin.time.Clock.System.now(),
            updatedAt = kotlin.time.Clock.System.now()
        )

        every { repository.validateAndGetUser(token1) } returns ApiCallResult.Success(user1)
        every { repository.validateAndGetUser(token2) } returns ApiCallResult.Success(user2)
        every { repository.generateRefreshToken(1) } returns "new-token1"
        every { repository.generateRefreshToken(2) } returns "new-token2"
        every { repository.revokeToken(any()) } returns ApiCallResult.Success(Unit)

        // When
        val result1 = service.refresh(token1)
        val result2 = service.refresh(token2)

        // Then
        result1.shouldBeInstanceOf<ApiCallResult.Success<TokenResponse>>()
        result2.shouldBeInstanceOf<ApiCallResult.Success<TokenResponse>>()

        val accessToken1 = result1.data.accessToken
        val accessToken2 = result2.data.accessToken

        // Access tokens should be different for different users
        accessToken1 shouldNotBe accessToken2
    }

    @Test
    @DisplayName("should return Failure when user has no ID")
    fun `refresh with user missing ID returns Failure`() {
        // Given
        val token = "token-with-no-user-id"
        val userWithNoId = UserDTO(
            id = null,
            username = "userNoId",
            password = "password",
            createdAt = kotlin.time.Clock.System.now(),
            updatedAt = kotlin.time.Clock.System.now()
        )

        every { repository.validateAndGetUser(token) } returns ApiCallResult.Success(userWithNoId)

        // When
        val result = service.refresh(token)

        // Then
        result.shouldBeInstanceOf<ApiCallResult.Failure>()
        result.reason shouldBe "User ID not found"

        // Verify no token generation or revocation occurred
        verify(exactly = 1) { repository.validateAndGetUser(token) }
        verify(exactly = 0) { repository.generateRefreshToken(any()) }
        verify(exactly = 0) { repository.revokeToken(any()) }
    }
}
