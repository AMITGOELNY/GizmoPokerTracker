package com.ghn.poker.feature.feed.data.repository

import com.ghn.gizmodb.common.models.FeedDTO
import com.ghn.gizmodb.common.models.NewsCategory
import com.ghn.poker.core.network.ApiResponse
import com.ghn.poker.feature.feed.data.sources.remote.FeedRemoteDataSource
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.time.Instant

class FeedRepositoryImplTest {

    @Test
    fun getFeed_delegates_to_remote_data_source_and_returns_success() = runTest {
        val remoteDataSource = mock<FeedRemoteDataSource>()
        val repository = FeedRepositoryImpl(remoteDataSource)
        val feedItems = listOf(
            createFeedDTO(1, "news1", NewsCategory.NEWS),
            createFeedDTO(2, "strategy1", NewsCategory.STRATEGY),
        )
        val expectedResponse = ApiResponse.Success(feedItems)

        everySuspend { remoteDataSource.getFeed() } returns expectedResponse

        val result = repository.getFeed()

        result shouldBe expectedResponse
        verifySuspend { remoteDataSource.getFeed() }
    }

    @Test
    fun getFeed_returns_http_error_from_remote_data_source() = runTest {
        val remoteDataSource = mock<FeedRemoteDataSource>()
        val repository = FeedRepositoryImpl(remoteDataSource)
        val expectedError = ApiResponse.Error.HttpError(500, Exception("Internal Server Error"))

        everySuspend { remoteDataSource.getFeed() } returns expectedError

        val result = repository.getFeed()

        result shouldBe expectedError
        verifySuspend { remoteDataSource.getFeed() }
    }

    @Test
    fun getFeed_returns_network_error_from_remote_data_source() = runTest {
        val remoteDataSource = mock<FeedRemoteDataSource>()
        val repository = FeedRepositoryImpl(remoteDataSource)

        everySuspend { remoteDataSource.getFeed() } returns ApiResponse.Error.NetworkError

        val result = repository.getFeed()

        result shouldBe ApiResponse.Error.NetworkError
        verifySuspend { remoteDataSource.getFeed() }
    }

    @Test
    fun getFeed_returns_serialization_error_from_remote_data_source() = runTest {
        val remoteDataSource = mock<FeedRemoteDataSource>()
        val repository = FeedRepositoryImpl(remoteDataSource)

        everySuspend { remoteDataSource.getFeed() } returns ApiResponse.Error.SerializationError

        val result = repository.getFeed()

        result shouldBe ApiResponse.Error.SerializationError
        verifySuspend { remoteDataSource.getFeed() }
    }

    @Test
    fun getFeed_returns_empty_list_on_success_with_no_items() = runTest {
        val remoteDataSource = mock<FeedRemoteDataSource>()
        val repository = FeedRepositoryImpl(remoteDataSource)
        val expectedResponse = ApiResponse.Success(emptyList<FeedDTO>())

        everySuspend { remoteDataSource.getFeed() } returns expectedResponse

        val result = repository.getFeed()

        result shouldBe expectedResponse
        verifySuspend { remoteDataSource.getFeed() }
    }

    private fun createFeedDTO(id: Int, identifier: String, category: NewsCategory): FeedDTO {
        return FeedDTO(
            id = id,
            link = "https://example.com/$identifier",
            image = "https://example.com/image/$identifier.jpg",
            title = "Title $identifier",
            pubDate = LocalDate(2024, 5, 10),
            category = category,
            site = "TestSite",
            createdAt = Instant.fromEpochMilliseconds(1000L * id),
            updatedAt = Instant.fromEpochMilliseconds(1000L * id + 1)
        )
    }
}
