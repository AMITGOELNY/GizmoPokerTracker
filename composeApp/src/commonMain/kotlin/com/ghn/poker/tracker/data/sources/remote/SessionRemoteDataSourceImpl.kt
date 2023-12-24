package com.ghn.poker.tracker.data.sources.remote

import co.touchlab.kermit.Logger
import com.ghn.gizmodb.common.models.SessionDTO
import com.ghn.poker.tracker.data.api.KtorProvider
import io.ktor.client.HttpClient
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.http.URLProtocol
import io.ktor.http.path
import io.ktor.utils.io.errors.IOException
import kotlinx.serialization.SerializationException
import org.koin.core.annotation.Single

@Single([SessionRemoteDataSource::class])
internal class SessionRemoteDataSourceImpl(
    private val provider: KtorProvider
) : SessionRemoteDataSource {
    override suspend fun getSessions(): ApiResponse<List<SessionDTO>, Exception> {
        return provider.client.safeRequest {
            get {
                url {
                    protocol = URLProtocol.HTTP
                    host = "138.197.84.104"
                    path("sessions")
                }
            }.body<List<SessionDTO>>()
        }
    }
}

suspend inline fun <reified T, reified E> HttpClient.safeRequest(
    block: HttpClient.() -> T,
): ApiResponse<T, E> =
    try {
        val response = block()
        ApiResponse.Success(response)
    } catch (e: ClientRequestException) {
        ApiResponse.Error.HttpError(e.response.status.value, e.errorBody())
    } catch (e: ServerResponseException) {
        ApiResponse.Error.HttpError(e.response.status.value, e.errorBody())
    } catch (e: IOException) {
        ApiResponse.Error.NetworkError
    } catch (e: SerializationException) {
        ApiResponse.Error.SerializationError
    } catch (e: NoTransformationFoundException) {
        Logger.e("yolo", e)
        ApiResponse.Error.SerializationError
    }

suspend inline fun <reified E> ResponseException.errorBody(): E? =
    try {
        response.body()
    } catch (e: SerializationException) {
        null
    }

sealed class ApiResponse<out T, out E> {
    /** Represents successful network responses (2xx). */
    data class Success<T>(val body: T) : ApiResponse<T, Nothing>()

    sealed class Error<E> : ApiResponse<Nothing, E>() {
        /** Represents server (50x) and client (40x) errors. */
        data class HttpError<E>(val code: Int, val errorBody: E?) : Error<E>()

        /** Represent IOExceptions and connectivity issues. */
        data object NetworkError : Error<Nothing>()

        /** Represent SerializationExceptions. */
        data object SerializationError : Error<Nothing>()
    }
}
