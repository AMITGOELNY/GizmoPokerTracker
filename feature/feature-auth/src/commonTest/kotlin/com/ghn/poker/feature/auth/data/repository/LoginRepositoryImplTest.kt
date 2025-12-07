@file:Suppress("ktlint:standard:no-empty-file")

package com.ghn.poker.feature.auth.data.repository

// TODO: Re-enable once mokkery is updated for kotlin 2.3+ support
/*
import com.ghn.model.TokenResponse
import com.ghn.poker.core.network.ApiResponse
import com.ghn.poker.feature.auth.data.sources.remote.LoginRemoteDataSource
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class LoginRepositoryImplTest {

    @Test
    fun login_returns_success_when_remote_data_source_succeeds() = runTest {
        val remoteDataSource = mock<LoginRemoteDataSource>()
        val repository = LoginRepositoryImpl(remoteDataSource)
        val tokenResponse = TokenResponse(
            accessToken = "access-token-12345",
            refreshToken = "refresh-token-67890"
        )

        everySuspend { remoteDataSource.login("user", "pass") } returns ApiResponse.Success(tokenResponse)

        val result = repository.login("user", "pass")

        result shouldBe ApiResponse.Success(Unit)
        verifySuspend { remoteDataSource.login("user", "pass") }
    }

    @Test
    fun login_returns_error_when_remote_data_source_fails() = runTest {
        val remoteDataSource = mock<LoginRemoteDataSource>()
        val repository = LoginRepositoryImpl(remoteDataSource)

        everySuspend { remoteDataSource.login("user", "pass") } returns ApiResponse.Error.NetworkError

        val result = repository.login("user", "pass")

        result shouldBe ApiResponse.Error.NetworkError
        verifySuspend { remoteDataSource.login("user", "pass") }
    }

    @Test
    fun login_returns_http_error_from_remote_data_source() = runTest {
        val remoteDataSource = mock<LoginRemoteDataSource>()
        val repository = LoginRepositoryImpl(remoteDataSource)
        val httpError = ApiResponse.Error.HttpError(401, Exception("Invalid credentials"))

        everySuspend { remoteDataSource.login("user", "wrong") } returns httpError

        val result = repository.login("user", "wrong")

        result shouldBe httpError
        verifySuspend { remoteDataSource.login("user", "wrong") }
    }

    @Test
    fun create_returns_success_when_remote_data_source_succeeds() = runTest {
        val remoteDataSource = mock<LoginRemoteDataSource>()
        val repository = LoginRepositoryImpl(remoteDataSource)
        val tokenResponse = TokenResponse(
            accessToken = "new-access-token",
            refreshToken = "new-refresh-token"
        )

        everySuspend { remoteDataSource.create("newuser", "newpass") } returns ApiResponse.Success(tokenResponse)

        val result = repository.create("newuser", "newpass")

        result shouldBe ApiResponse.Success(Unit)
        verifySuspend { remoteDataSource.create("newuser", "newpass") }
    }

    @Test
    fun create_returns_error_when_remote_data_source_fails() = runTest {
        val remoteDataSource = mock<LoginRemoteDataSource>()
        val repository = LoginRepositoryImpl(remoteDataSource)

        everySuspend { remoteDataSource.create("newuser", "newpass") } returns ApiResponse.Error.NetworkError

        val result = repository.create("newuser", "newpass")

        result shouldBe ApiResponse.Error.NetworkError
        verifySuspend { remoteDataSource.create("newuser", "newpass") }
    }

    @Test
    fun logout_delegates_to_remote_data_source() = runTest {
        val remoteDataSource = mock<LoginRemoteDataSource>()
        val repository = LoginRepositoryImpl(remoteDataSource)

        everySuspend { remoteDataSource.logout() } returns ApiResponse.Success(Unit)

        val result = repository.logout()

        result shouldBe ApiResponse.Success(Unit)
        verifySuspend { remoteDataSource.logout() }
    }

    @Test
    fun logout_returns_error_when_remote_data_source_fails() = runTest {
        val remoteDataSource = mock<LoginRemoteDataSource>()
        val repository = LoginRepositoryImpl(remoteDataSource)

        everySuspend { remoteDataSource.logout() } returns ApiResponse.Error.NetworkError

        val result = repository.logout()

        result shouldBe ApiResponse.Error.NetworkError
        verifySuspend { remoteDataSource.logout() }
    }
}
*/
