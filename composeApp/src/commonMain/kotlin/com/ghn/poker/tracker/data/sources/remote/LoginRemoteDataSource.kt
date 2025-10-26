package com.ghn.poker.tracker.data.sources.remote

import com.ghn.poker.tracker.data.api.GizmoApiClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.koin.core.annotation.Single

interface LoginRemoteDataSource {
    suspend fun login(username: String, password: String): ApiResponse<String, Exception>

    suspend fun create(username: String, password: String): ApiResponse<String, Exception>
}

@Single([LoginRemoteDataSource::class])
internal class LoginRemoteDataSourceImpl(
    private val apiClient: GizmoApiClient
) : LoginRemoteDataSource {
    override suspend fun login(username: String, password: String): ApiResponse<String, Exception> {
        return apiClient.http.safeRequest {
            val response = post("login") {
                setBody(mapOf("username" to username, "password" to password))
            }
            val token = response.body<String>()
            apiClient.storeToken(token)
            token
        }
    }

    override suspend fun create(
        username: String,
        password: String
    ): ApiResponse<String, Exception> {
        val result: ApiResponse<String, Exception> = apiClient.http.safeRequest {
            val response = post("createUser") {
                setBody(mapOf("username" to username, "password" to password))
            }

            response.body<String>()
        }
        return if (result is ApiResponse.Success) login(username, password) else result
    }
}
