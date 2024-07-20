package com.ghn.poker.tracker.data.sources.remote

import com.ghn.gizmodb.common.models.FeedDTO
import com.ghn.poker.tracker.data.api.GizmoApiClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.path
import org.koin.core.annotation.Single

interface FeedRemoteDataSource {
    suspend fun getFeed(): ApiResponse<List<FeedDTO>, Exception>
}

@Single([FeedRemoteDataSource::class])
internal class FeedRemoteDataSourceImpl(
    private val apiClient: GizmoApiClient
) : FeedRemoteDataSource {
    override suspend fun getFeed(): ApiResponse<List<FeedDTO>, Exception> {
        return try {
            return apiClient.http.safeRequest {
                get { url { path("feed") } }
                    .body<List<FeedDTO>>()
            }
        } catch (e: Exception) {
            ApiResponse.Error.NetworkError
        }
    }
}
