package com.ghn.poker.tracker.data.sources.remote

import com.ghn.poker.tracker.data.api.GizmoApiClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.path
import org.koin.core.annotation.Single

@Single([LoginRemoteDataSource::class])
internal class LoginRemoteDataSourceImpl(
    private val apiClient: GizmoApiClient
) : LoginRemoteDataSource {
    override suspend fun login(username: String, password: String): ApiResponse<String, Exception> {
        return apiClient.http.safeRequest {
            val response = post {
                url { path("login") }
                setBody(mapOf("username" to username, "password" to password))
            }
            val token = response.body<String>()
            apiClient.storeToken(token)
            token
        }
    }
}
