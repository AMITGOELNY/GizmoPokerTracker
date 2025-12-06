package com.ghn.poker.feature.feed.domain.repository

import com.ghn.gizmodb.common.models.FeedDTO
import com.ghn.poker.core.network.ApiResponse

interface FeedRepository {
    suspend fun getFeed(): ApiResponse<List<FeedDTO>, Exception>
}
