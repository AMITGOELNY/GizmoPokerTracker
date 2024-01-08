package com.ghn.poker.tracker.data.api

import com.ghn.poker.tracker.data.preferences.PreferenceManager
import com.ghn.poker.tracker.data.preferences.Preferences
import com.ghn.poker.tracker.data.preferences.get
import com.ghn.poker.tracker.data.preferences.set
import io.ktor.client.HttpClient
import io.ktor.client.plugins.addDefaultResponseValidation
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Single

private const val BASE_URL = "138.197.84.104"

@Single([GizmoApiClient::class])
internal class GizmoApiClient(
    httpClient: HttpClient,
    private val preferenceManager: PreferenceManager
) {
    val http = httpClient.config {
        install(Logging) {
            logger = Logger.SIMPLE
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
            url {
                protocol = URLProtocol.HTTP
                host = BASE_URL
            }
        }
        expectSuccess = true
        addDefaultResponseValidation()

        install("TokenHandler") {
            requestPipeline.intercept(HttpRequestPipeline.Before) {
                if (context.url.pathSegments.first() != "login") {
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
}
