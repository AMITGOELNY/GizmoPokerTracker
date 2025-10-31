package com.ghn.poker.tracker.data.sources.remote

import com.ghn.model.RefreshTokenRequest
import com.ghn.model.TokenResponse
import com.ghn.poker.tracker.data.api.GizmoApiClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.koin.core.annotation.Single

interface RefreshRemoteDataSource {
    suspend fun refreshTokens(refreshToken: String): ApiResponse<TokenResponse, Exception>
}

@Single([RefreshRemoteDataSource::class])
internal class RefreshRemoteDataSourceImpl(
    private val apiClient: GizmoApiClient
) : RefreshRemoteDataSource {
    override suspend fun refreshTokens(refreshToken: String): ApiResponse<TokenResponse, Exception> {
        return apiClient.http.safeRequest {
            val response = post("refresh") {
                setBody(RefreshTokenRequest(refreshToken))
            }
            val tokenResponse = response.body<TokenResponse>()
            apiClient.storeTokens(tokenResponse.accessToken, tokenResponse.refreshToken)
            tokenResponse
        }
    }
}
