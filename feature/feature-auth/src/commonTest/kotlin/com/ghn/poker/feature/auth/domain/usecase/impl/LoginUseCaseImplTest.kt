package com.ghn.poker.feature.auth.domain.usecase.impl

import com.ghn.poker.core.network.ApiResponse
import com.ghn.poker.feature.auth.domain.repository.LoginRepository
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class LoginUseCaseImplTest {

    @Test
    fun login_delegates_to_repository_and_returns_result() = runTest {
        val loginRepository = mock<LoginRepository>()
        val useCase = LoginUseCaseImpl(loginRepository)
        val username = "user"
        val password = "pass"
        val expected = ApiResponse.Success(Unit)

        everySuspend { loginRepository.login(username, password) } returns expected

        val result = useCase.login(username, password)

        result shouldBe expected
        verifySuspend { loginRepository.login(username, password) }
    }

    @Test
    fun login_returns_network_error_when_repository_fails() = runTest {
        val loginRepository = mock<LoginRepository>()
        val useCase = LoginUseCaseImpl(loginRepository)

        everySuspend { loginRepository.login("user", "pass") } returns ApiResponse.Error.NetworkError

        val result = useCase.login("user", "pass")

        result shouldBe ApiResponse.Error.NetworkError
        verifySuspend { loginRepository.login("user", "pass") }
    }

    @Test
    fun login_returns_http_error_from_repository() = runTest {
        val loginRepository = mock<LoginRepository>()
        val useCase = LoginUseCaseImpl(loginRepository)
        val httpError = ApiResponse.Error.HttpError(401, Exception("Unauthorized"))

        everySuspend { loginRepository.login("user", "wrong") } returns httpError

        val result = useCase.login("user", "wrong")

        result shouldBe httpError
        verifySuspend { loginRepository.login("user", "wrong") }
    }
}
