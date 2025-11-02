package com.ghn.poker.tracker.data.api

import co.touchlab.kermit.Logger
import com.ghn.model.RefreshTokenRequest
import com.ghn.model.TokenResponse
import com.ghn.poker.tracker.data.preferences.PreferenceManager
import com.ghn.poker.tracker.data.preferences.Preferences
import com.ghn.poker.tracker.data.preferences.get
import com.ghn.poker.tracker.data.preferences.set
import com.ghn.poker.tracker.data.sources.remote.ApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.addDefaultResponseValidation
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Single
import io.ktor.client.plugins.logging.Logger as KtorLogger

private const val BASE_URL = "gizmopoker.xyz"
// private const val BASE_URL = "10.0.2.2:8080"

@Single([GizmoApiClient::class])
internal class GizmoApiClient(
    httpClient: HttpClient,
    private val preferenceManager: PreferenceManager
) {
    private val refreshMutex = Mutex()
    private var isRefreshing = false

    val http = httpClient.config {
        install(Logging) {
            logger = KtorLogger.SIMPLE
            level = LogLevel.BODY
        }

        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    encodeDefaults = true
                    isLenient = true
                }
            )
        }
        defaultRequest {
            contentType(ContentType.Application.Json)
            url("https://$BASE_URL/poker-api/")
        }
        expectSuccess = true
        addDefaultResponseValidation()

        install("TokenHandler") {
            requestPipeline.intercept(HttpRequestPipeline.Before) {
                if (!context.url.pathSegments.contains("login") &&
                    !context.url.pathSegments.contains("refresh")
                ) {
                    preferenceManager.get<String>(Preferences.USER_TOKEN_KEY)?.let {
                        context.header("Authorization", "Bearer $it")
                    }
                }
            }
        }
    }

    fun storeToken(token: String) {
        preferenceManager[Preferences.USER_TOKEN_KEY] = token
    }

    fun storeTokens(accessToken: String, refreshToken: String) {
        preferenceManager[Preferences.USER_TOKEN_KEY] = accessToken
        preferenceManager[Preferences.REFRESH_TOKEN_KEY] = refreshToken
    }

    fun getRefreshToken(): String? {
        return preferenceManager.get<String>(Preferences.REFRESH_TOKEN_KEY)
    }

    fun clearTokens() {
        preferenceManager[Preferences.USER_TOKEN_KEY] = ""
        preferenceManager[Preferences.REFRESH_TOKEN_KEY] = ""
    }

    private suspend fun attemptTokenRefresh(refreshToken: String): ApiResponse<TokenResponse, Exception> {
        return try {
            val response = http.post("refresh") {
                setBody(RefreshTokenRequest(refreshToken))
            }

            if (response.status == HttpStatusCode.OK) {
                val tokenResponse = response.body<TokenResponse>()
                storeTokens(tokenResponse.accessToken, tokenResponse.refreshToken)
                Logger.d { "Token refresh successful" }
                ApiResponse.Success(tokenResponse)
            } else {
                Logger.d { "Token refresh failed with status: ${response.status}" }
                ApiResponse.Error.HttpError(response.status.value, null)
            }
        } catch (e: Exception) {
            Logger.e("Token refresh error", e)
            ApiResponse.Error.NetworkError
        }
    }

    suspend fun <T> executeWithAutoRefresh(
        block: suspend () -> ApiResponse<T, Exception>
    ): ApiResponse<T, Exception> {
        val result = block()

        // If not a 401 error, return as-is
        if (result !is ApiResponse.Error.HttpError || result.code != 401) {
            return result
        }

        Logger.d { "Received 401, attempting token refresh" }

        // Attempt to refresh token using mutex to prevent concurrent attempts
        val refreshResult = refreshMutex.withLock {
            val refreshToken = getRefreshToken()
            if (refreshToken.isNullOrEmpty()) {
                Logger.d { "No refresh token available" }
                clearTokens()
                return@withLock null
            }

            attemptTokenRefresh(refreshToken)
        }

        return when (refreshResult) {
            is ApiResponse.Success -> {
                Logger.d { "Token refreshed, retrying original request" }
                // Retry the original request with new token
                block()
            }

            is ApiResponse.Error, null -> {
                Logger.d { "Token refresh failed, clearing tokens" }
                clearTokens()
                result
            }
        }
    }
}
