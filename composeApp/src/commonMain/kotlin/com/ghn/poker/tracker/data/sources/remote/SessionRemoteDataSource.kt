package com.ghn.poker.tracker.data.sources.remote

import com.ghn.gizmodb.common.models.SessionDTO
import com.ghn.poker.tracker.data.api.GizmoApiClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.path
import org.koin.core.annotation.Single

interface SessionRemoteDataSource {
    suspend fun getSessions(): ApiResponse<List<SessionDTO>, Exception>
    suspend fun createSession(session: SessionDTO): ApiResponse<Unit, Exception>
}

@Single([SessionRemoteDataSource::class])
internal class SessionRemoteDataSourceImpl(
    private val apiClient: GizmoApiClient
) : SessionRemoteDataSource {
    override suspend fun getSessions(): ApiResponse<List<SessionDTO>, Exception> {
        return apiClient.http.safeRequest {
            get { url { path("sessions") } }
                .body<List<SessionDTO>>()
        }
    }

    override suspend fun createSession(
        session: SessionDTO,
    ): ApiResponse<Unit, Exception> {
        return apiClient.http.safeRequest {
            post {
                url { path("sessions") }
                setBody(session)
            }.body<String>()
        }
    }
}
