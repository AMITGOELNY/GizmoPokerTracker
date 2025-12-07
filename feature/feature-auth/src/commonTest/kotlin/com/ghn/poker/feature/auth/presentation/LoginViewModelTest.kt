@file:Suppress("ktlint:standard:no-empty-file")

package com.ghn.poker.feature.auth.presentation

// TODO: Re-enable once mokkery is updated for kotlin 2.3+ support
/*
import com.ghn.poker.core.network.ApiResponse
import com.ghn.poker.core.testing.BaseViewModelTest
import com.ghn.poker.feature.auth.domain.usecase.LoginUseCase
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldBeEmpty
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class LoginViewModelTest : BaseViewModelTest() {

    private lateinit var loginUseCase: LoginUseCase

    override fun onSetup() {
        loginUseCase = mock<LoginUseCase>()
    }

    @Test
    fun initial_state_has_empty_credentials_and_not_authenticating() = runTest(testDispatcher) {
        val viewModel = LoginViewModel(loginUseCase)
        advanceUntilIdle()

        val state = viewModel.state.value
        state.username.shouldBeEmpty()
        state.password.shouldBeEmpty()
        state.authenticating.shouldBeFalse()
    }

    @Test
    fun dispatch_OnUsernameChange_updates_username() = runTest(testDispatcher) {
        val viewModel = LoginViewModel(loginUseCase)
        advanceUntilIdle()

        viewModel.onDispatch(LoginAction.OnUsernameChange("testuser"))
        advanceUntilIdle()

        viewModel.state.value.username shouldBe "testuser"
    }

    @Test
    fun dispatch_OnPasswordChange_updates_password() = runTest(testDispatcher) {
        val viewModel = LoginViewModel(loginUseCase)
        advanceUntilIdle()

        viewModel.onDispatch(LoginAction.OnPasswordChange("secret123"))
        advanceUntilIdle()

        viewModel.state.value.password shouldBe "secret123"
    }

    @Test
    fun dispatch_OnSubmit_calls_login_usecase_with_credentials() = runTest(testDispatcher) {
        everySuspend { loginUseCase.login(any(), any()) } returns ApiResponse.Success(Unit)

        val viewModel = LoginViewModel(loginUseCase)
        advanceUntilIdle()

        viewModel.onDispatch(LoginAction.OnUsernameChange("user"))
        advanceUntilIdle()
        viewModel.onDispatch(LoginAction.OnPasswordChange("pass"))
        advanceUntilIdle()

        viewModel.onDispatch(LoginAction.OnSubmit)
        advanceUntilIdle()

        verifySuspend { loginUseCase.login("user", "pass") }
    }

    @Test
    fun dispatch_OnSubmit_sets_authenticating_during_login() = runTest(testDispatcher) {
        everySuspend { loginUseCase.login(any(), any()) } returns ApiResponse.Success(Unit)

        val viewModel = LoginViewModel(loginUseCase)
        advanceUntilIdle()

        viewModel.onDispatch(LoginAction.OnUsernameChange("user"))
        advanceUntilIdle()
        viewModel.onDispatch(LoginAction.OnPasswordChange("pass"))
        advanceUntilIdle()

        viewModel.onDispatch(LoginAction.OnSubmit)
        advanceUntilIdle()

        // After login completes, authenticating should be false
        viewModel.state.value.authenticating.shouldBeFalse()
    }

    @Test
    fun dispatch_OnSubmit_emits_NavigateToDashboard_on_success() = runTest(testDispatcher) {
        everySuspend { loginUseCase.login(any(), any()) } returns ApiResponse.Success(Unit)

        val viewModel = LoginViewModel(loginUseCase)
        advanceUntilIdle()

        // Start collecting effects before triggering the action
        var receivedEffect: LoginEffect? = null
        val job = backgroundScope.launch {
            viewModel.effects.first().also { receivedEffect = it }
        }
        advanceUntilIdle()

        viewModel.onDispatch(LoginAction.OnUsernameChange("user"))
        advanceUntilIdle()
        viewModel.onDispatch(LoginAction.OnPasswordChange("pass"))
        advanceUntilIdle()

        viewModel.onDispatch(LoginAction.OnSubmit)
        advanceUntilIdle()

        job.join()
        receivedEffect shouldBe LoginEffect.NavigateToDashboard
    }

    @Test
    fun dispatch_OnSubmit_clears_authenticating_on_error() = runTest(testDispatcher) {
        everySuspend { loginUseCase.login(any(), any()) } returns ApiResponse.Error.NetworkError

        val viewModel = LoginViewModel(loginUseCase)
        advanceUntilIdle()

        viewModel.onDispatch(LoginAction.OnUsernameChange("user"))
        advanceUntilIdle()
        viewModel.onDispatch(LoginAction.OnPasswordChange("pass"))
        advanceUntilIdle()

        viewModel.onDispatch(LoginAction.OnSubmit)
        advanceUntilIdle()

        viewModel.state.value.authenticating.shouldBeFalse()
    }
}
*/
