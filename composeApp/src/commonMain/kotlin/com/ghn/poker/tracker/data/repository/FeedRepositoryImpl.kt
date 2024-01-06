package com.ghn.poker.tracker.data.repository

import com.ghn.gizmodb.common.models.FeedDTO
import com.ghn.poker.tracker.data.sources.remote.ApiResponse
import com.ghn.poker.tracker.data.sources.remote.FeedRemoteDataSource
import com.ghn.poker.tracker.domain.repository.FeedRepository
import org.koin.core.annotation.Single

@Single([FeedRepository::class])
class FeedRepositoryImpl(
    private val remoteDataSource: FeedRemoteDataSource
) : FeedRepository {
    override suspend fun getFeed(): ApiResponse<List<FeedDTO>, Exception> {
        return remoteDataSource.getFeed()
    }
}
