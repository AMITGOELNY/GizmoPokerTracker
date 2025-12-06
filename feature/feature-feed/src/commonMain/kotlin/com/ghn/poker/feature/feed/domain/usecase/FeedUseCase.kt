package com.ghn.poker.feature.feed.domain.usecase

import com.ghn.poker.core.network.ApiResponse
import com.ghn.poker.feature.feed.domain.model.FeedItem

interface FeedUseCase {
    suspend fun getFeed(): ApiResponse<List<FeedItem>, Exception>
}
