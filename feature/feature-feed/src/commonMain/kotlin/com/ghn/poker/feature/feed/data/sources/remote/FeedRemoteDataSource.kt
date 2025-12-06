package com.ghn.poker.feature.feed.data.sources.remote

import com.ghn.gizmodb.common.models.FeedDTO
import com.ghn.poker.core.network.ApiResponse
import com.ghn.poker.core.network.GizmoApiClient
import com.ghn.poker.core.network.safeRequest
import io.ktor.client.call.body
import io.ktor.client.request.get
import org.koin.core.annotation.Single

interface FeedRemoteDataSource {
    suspend fun getFeed(): ApiResponse<List<FeedDTO>, Exception>
}

@Single([FeedRemoteDataSource::class])
internal class FeedRemoteDataSourceImpl(
    private val apiClient: GizmoApiClient
) : FeedRemoteDataSource {
    override suspend fun getFeed(): ApiResponse<List<FeedDTO>, Exception> {
        return apiClient.executeWithAutoRefresh {
            apiClient.http.safeRequest {
                get("feed").body<List<FeedDTO>>()
            }
        }
    }
}
