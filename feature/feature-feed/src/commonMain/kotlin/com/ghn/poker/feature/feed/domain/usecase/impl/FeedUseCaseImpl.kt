package com.ghn.poker.feature.feed.domain.usecase.impl

import com.ghn.poker.core.network.ApiResponse
import com.ghn.poker.feature.feed.domain.model.FeedItem
import com.ghn.poker.feature.feed.domain.repository.FeedRepository
import com.ghn.poker.feature.feed.domain.usecase.FeedUseCase
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
                            category = it.category,
                        )
                    }
                        .sortedByDescending { it.pubDate }
                )
            }
        }
    }
}
