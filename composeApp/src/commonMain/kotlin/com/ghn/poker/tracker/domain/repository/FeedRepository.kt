package com.ghn.poker.tracker.domain.repository

import com.ghn.gizmodb.common.models.FeedDTO
import com.ghn.poker.tracker.data.sources.remote.ApiResponse

interface FeedRepository {
    suspend fun getFeed(): ApiResponse<List<FeedDTO>, Exception>
}
