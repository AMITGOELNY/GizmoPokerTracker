package com.ghn.poker.tracker.data.api

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Single

internal interface KtorProvider {
    val client: HttpClient

    fun <T : HttpClientEngineConfig> HttpClientConfig<T>.getCommonConfig() {
        install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.BODY
        }

        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    encodeDefaults = true
                }
            )
        }
        defaultRequest {
            contentType(ContentType.Application.Json)
        }
    }

//    fun HttpRequestBuilder.apiUrl(path: String, baseUrl: String = ) {
//        url {
//            takeFrom(baseUrl)
//            encodedPath = path
//        }
//    }
//
//    fun HttpRequestBuilder.apiHeaders(
//        headers: Map<String, String> = emptyMap(),
//        token: String
//    ): HeadersBuilder =
//        headers {
//            append("Authorization", token)
//            for (header in headers) {
//                append(header.key, header.value)
//            }
//        }
}

@Single([KtorProvider::class])
internal expect class KtorProviderImpl() : KtorProvider
