package com.ghn.poker.feature.feed.domain.usecase.impl

import com.ghn.gizmodb.common.models.FeedDTO
import com.ghn.gizmodb.common.models.NewsCategory
import com.ghn.poker.core.network.ApiResponse
import com.ghn.poker.feature.feed.domain.model.FeedItem
import com.ghn.poker.feature.feed.domain.repository.FeedRepository
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.time.Instant

class FeedUseCaseImplTest {

    @Test
    fun getFeed_returns_sorted_mapped_items_on_success() = runTest {
        val repository = mock<FeedRepository>()
        val useCase = FeedUseCaseImpl(repository)
        val newerDate = LocalDate(2024, 5, 12)
        val olderDate = LocalDate(2024, 5, 1)
        val dtoNewer = FeedDTO(
            id = 1,
            link = "link-new",
            image = "image-new",
            title = "New Title",
            pubDate = newerDate,
            category = NewsCategory.NEWS,
            site = "SiteA",
            createdAt = Instant.fromEpochMilliseconds(10),
            updatedAt = Instant.fromEpochMilliseconds(11)
        )
        val dtoOlder = FeedDTO(
            id = 2,
            link = "link-old",
            image = "image-old",
            title = "Old Title",
            pubDate = olderDate,
            category = NewsCategory.STRATEGY,
            site = "SiteB",
            createdAt = Instant.fromEpochMilliseconds(0),
            updatedAt = Instant.fromEpochMilliseconds(1)
        )
        val expectedItems = listOf(
            FeedItem(
                link = dtoNewer.link,
                image = dtoNewer.image,
                title = dtoNewer.title,
                pubDate = dtoNewer.pubDate,
                category = dtoNewer.category,
                site = dtoNewer.site
            ),
            FeedItem(
                link = dtoOlder.link,
                image = dtoOlder.image,
                title = dtoOlder.title,
                pubDate = dtoOlder.pubDate,
                category = dtoOlder.category,
                site = dtoOlder.site
            )
        )

        everySuspend {
            repository.getFeed()
        } returns ApiResponse.Success(listOf(dtoOlder, dtoNewer))

        val result = useCase.getFeed()

        result shouldBe ApiResponse.Success(expectedItems)
        verifySuspend { repository.getFeed() }
    }

    @Test
    fun getFeed_returns_error_when_repository_fails() = runTest {
        val repository = mock<FeedRepository>()
        val useCase = FeedUseCaseImpl(repository)
        val error = ApiResponse.Error.HttpError(code = 500, errorBody = Exception("boom"))

        everySuspend { repository.getFeed() } returns error

        val result = useCase.getFeed()

        result shouldBe error
        verifySuspend { repository.getFeed() }
    }
}
