package com.ghn.poker.tracker.domain.usecase

import com.ghn.poker.tracker.data.sources.remote.ApiResponse
import com.ghn.poker.tracker.domain.model.FeedItem

interface FeedUseCase {
    suspend fun getFeed(): ApiResponse<List<FeedItem>, Exception>
}
