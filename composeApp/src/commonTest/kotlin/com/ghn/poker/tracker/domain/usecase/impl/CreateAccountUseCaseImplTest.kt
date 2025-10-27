package com.ghn.poker.tracker.domain.usecase.impl

import com.ghn.poker.tracker.data.sources.remote.ApiResponse
import com.ghn.poker.tracker.domain.repository.LoginRepository
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verify.VerifyMode
import dev.mokkery.verifySuspend
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class CreateAccountUseCaseImplTest {

    private val loginRepository: LoginRepository = mock<LoginRepository>(MockMode.autoUnit)
    private val useCase = CreateAccountUseCaseImpl(loginRepository)

    @Test
    fun create_delegates_to_repository_and_returns_result() = runTest {
        val username = "test-user"
        val password = "super-secret"
        val expected = ApiResponse.Success(Unit)

        everySuspend { loginRepository.create(username, password) } returns expected

        val result = useCase.create(username, password)
        result shouldBe expected

        verifySuspend(VerifyMode.exactly(1)) { loginRepository.create(username, password) }
    }
}
