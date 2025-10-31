package com.ghn.service

import com.ghn.model.TokenResponse
import com.ghn.model.User
import com.ghn.repository.ApiCallResult
import com.ghn.repository.UserRepository
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("UserServiceImpl")
class UserServiceImplTest {
    private lateinit var repository: UserRepository
    private lateinit var service: UserServiceImpl

    @BeforeEach
    fun setup() {
        repository = mockk()
        service = UserServiceImpl(repository)
    }

    @Nested
    @DisplayName("login")
    inner class Login {
        @Test
        fun `should return success when repository returns success`() {
            val username = "testuser"
            val password = "testpass"
            val tokenResponse = TokenResponse("access-token", "refresh-token")
            every { repository.login(username, password) } returns ApiCallResult.Success(tokenResponse)

            val result = service.login(username, password)

            result shouldBe ApiCallResult.Success(tokenResponse)
            verify(exactly = 1) { repository.login(username, password) }
        }

        @Test
        fun `should return BadPassword when repository returns BadPassword`() {
            val username = "testuser"
            val password = "wrongpass"
            every { repository.login(username, password) } returns ApiCallResult.BadPassword

            val result = service.login(username, password)

            result shouldBe ApiCallResult.BadPassword
            verify(exactly = 1) { repository.login(username, password) }
        }

        @Test
        fun `should return NotFound when repository returns NotFound`() {
            val username = "nonexistent"
            val password = "testpass"
            every { repository.login(username, password) } returns ApiCallResult.NotFound

            val result = service.login(username, password)

            result shouldBe ApiCallResult.NotFound
            verify(exactly = 1) { repository.login(username, password) }
        }

        @Test
        fun `should return Failure when repository returns Failure`() {
            val username = "testuser"
            val password = "testpass"
            val failureReason = "Database error"
            every { repository.login(username, password) } returns ApiCallResult.Failure(failureReason)

            val result = service.login(username, password)

            result shouldBe ApiCallResult.Failure(failureReason)
            verify(exactly = 1) { repository.login(username, password) }
        }

        @Test
        fun `should return Unauthorized when repository returns Unauthorized`() {
            val username = "testuser"
            val password = "testpass"
            every { repository.login(username, password) } returns ApiCallResult.Unauthorized

            val result = service.login(username, password)

            result shouldBe ApiCallResult.Unauthorized
            verify(exactly = 1) { repository.login(username, password) }
        }
    }

    @Nested
    @DisplayName("create")
    inner class Create {
        @Test
        fun `should return success when repository creates user successfully`() {
            val user = User(id = null, username = "newuser", password = "newpass")
            every { repository.create(user) } returns ApiCallResult.Success(Unit)

            val result = service.create(user)

            result shouldBe ApiCallResult.Success(Unit)
            verify(exactly = 1) { repository.create(user) }
        }

        @Test
        fun `should return Failure when repository fails to create user`() {
            val user = User(id = null, username = "existinguser", password = "newpass")
            val failureReason = "User already exists"
            every { repository.create(user) } returns ApiCallResult.Failure(failureReason)

            val result = service.create(user)

            result shouldBe ApiCallResult.Failure(failureReason)
            verify(exactly = 1) { repository.create(user) }
        }

        @Test
        fun `should propagate repository result for user with id`() {
            val user = User(id = 123, username = "existinguser", password = "password")
            every { repository.create(user) } returns ApiCallResult.Success(Unit)

            val result = service.create(user)

            result shouldBe ApiCallResult.Success(Unit)
            verify(exactly = 1) { repository.create(user) }
        }
    }
}
