package com.ghn.poker.tracker.domain.usecase.impl

import com.ghn.poker.tracker.data.sources.remote.ApiResponse
import com.ghn.poker.tracker.domain.model.FeedItem
import com.ghn.poker.tracker.domain.repository.FeedRepository
import com.ghn.poker.tracker.domain.usecase.FeedUseCase
import org.koin.core.annotation.Factory

@Factory([FeedUseCase::class])
class FeedUseCaseImpl(
    private val feedRepository: FeedRepository
) : FeedUseCase {
    override suspend fun getFeed(): ApiResponse<List<FeedItem>, Exception> {
        return when (val result = feedRepository.getFeed()) {
            is ApiResponse.Error -> result
            is ApiResponse.Success -> {
                ApiResponse.Success(
                    result.body.map {
                        FeedItem(
                            link = it.link,
                            image = it.image,
                            title = it.title,
                            pubDate = it.pubDate,
                            site = it.site,
                        )
                    }
                        .sortedByDescending { it.pubDate }
                )
            }
        }
    }
}
