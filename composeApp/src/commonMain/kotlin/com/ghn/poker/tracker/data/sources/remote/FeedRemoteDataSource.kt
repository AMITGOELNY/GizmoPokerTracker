package com.ghn.poker.tracker.data.sources.remote

import com.ghn.gizmodb.common.models.FeedDTO

interface FeedRemoteDataSource {
    suspend fun getFeed(): ApiResponse<List<FeedDTO>, Exception>
}
