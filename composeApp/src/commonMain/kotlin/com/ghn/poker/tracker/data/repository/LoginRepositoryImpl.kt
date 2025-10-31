package com.ghn.poker.tracker.data.repository

import co.touchlab.kermit.Logger
import com.ghn.poker.tracker.data.sources.remote.ApiResponse
import com.ghn.poker.tracker.data.sources.remote.LoginRemoteDataSource
import com.ghn.poker.tracker.domain.repository.LoginRepository
import org.koin.core.annotation.Single

@Single([LoginRepository::class])
class LoginRepositoryImpl(
    private val remoteDataSource: LoginRemoteDataSource
) : LoginRepository {
    override suspend fun login(username: String, password: String): ApiResponse<Unit, Exception> {
        return when (val result = remoteDataSource.login(username, password)) {
            is ApiResponse.Error -> result
            is ApiResponse.Success -> {
                Logger.d { "login success, tokens received: accessToken=${result.body.accessToken.take(20)}..., refreshToken=${result.body.refreshToken.take(20)}..." }
                ApiResponse.Success(Unit)
            }
        }
    }

    override suspend fun create(username: String, password: String): ApiResponse<Unit, Exception> {
        return when (val result = remoteDataSource.create(username, password)) {
            is ApiResponse.Error -> result
            is ApiResponse.Success -> {
                Logger.d { "create success, tokens received: accessToken=${result.body.accessToken.take(20)}..., refreshToken=${result.body.refreshToken.take(20)}..." }
                ApiResponse.Success(Unit)
            }
        }
    }
}
