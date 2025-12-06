package com.ghn.poker.feature.auth.data.sources.remote

import com.ghn.model.TokenResponse
import com.ghn.poker.core.network.ApiResponse
import com.ghn.poker.core.network.GizmoApiClient
import com.ghn.poker.core.network.safeRequest
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.koin.core.annotation.Single

interface LoginRemoteDataSource {
    suspend fun login(username: String, password: String): ApiResponse<TokenResponse, Exception>

    suspend fun create(username: String, password: String): ApiResponse<TokenResponse, Exception>

    suspend fun logout(): ApiResponse<Unit, Exception>
}

@Single([LoginRemoteDataSource::class])
internal class LoginRemoteDataSourceImpl(
    private val apiClient: GizmoApiClient
) : LoginRemoteDataSource {
    override suspend fun login(username: String, password: String): ApiResponse<TokenResponse, Exception> {
        return apiClient.http.safeRequest {
            val response = post("login") {
                setBody(mapOf("username" to username, "password" to password))
            }
            val tokenResponse = response.body<TokenResponse>()
            apiClient.storeTokens(tokenResponse.accessToken, tokenResponse.refreshToken)
            tokenResponse
        }
    }

    override suspend fun create(
        username: String,
        password: String
    ): ApiResponse<TokenResponse, Exception> {
        val result: ApiResponse<String, Exception> = apiClient.http.safeRequest {
            val response = post("createUser") {
                setBody(mapOf("username" to username, "password" to password))
            }

            response.body<String>()
        }
        return when (result) {
            is ApiResponse.Success -> login(username, password)
            is ApiResponse.Error -> result
        }
    }

    override suspend fun logout(): ApiResponse<Unit, Exception> {
        val result: ApiResponse<String, Exception> = apiClient.http.safeRequest {
            post("logout").body<String>()
        }

        // Clear tokens locally regardless of server response
        apiClient.clearTokens()

        return when (result) {
            is ApiResponse.Success -> ApiResponse.Success(Unit)
            is ApiResponse.Error -> result
        }
    }
}
