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
}
