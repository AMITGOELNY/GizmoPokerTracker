@file:Suppress("ktlint:standard:no-empty-file")

package com.ghn.poker.feature.feed.presentation

// TODO: Re-enable once mokkery is updated for kotlin 2.3+ support
/*
import com.ghn.gizmodb.common.models.NewsCategory
import com.ghn.poker.core.network.ApiResponse
import com.ghn.poker.core.testing.BaseViewModelTest
import com.ghn.poker.feature.feed.domain.model.FeedItem
import com.ghn.poker.feature.feed.domain.usecase.FeedUseCase
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import kotlin.test.Test

class FeedViewModelTest : BaseViewModelTest() {

    private lateinit var feedUseCase: FeedUseCase

    override fun onSetup() {
        feedUseCase = mock<FeedUseCase>()
    }

    @Test
    fun init_fetches_feed_and_updates_state_on_success() = runTest(testDispatcher) {
        val newsItems = listOf(
            createFeedItem("news1", NewsCategory.NEWS),
            createFeedItem("news2", NewsCategory.NEWS),
            createFeedItem("news3", NewsCategory.NEWS),
            createFeedItem("news4", NewsCategory.NEWS),
            createFeedItem("news5", NewsCategory.NEWS),
            createFeedItem("news6", NewsCategory.NEWS),
            createFeedItem("news7", NewsCategory.NEWS),
            createFeedItem("news8", NewsCategory.NEWS),
        )
        val strategyItems = listOf(
            createFeedItem("strategy1", NewsCategory.STRATEGY),
            createFeedItem("strategy2", NewsCategory.STRATEGY),
        )
        val allItems = newsItems + strategyItems

        everySuspend { feedUseCase.getFeed() } returns ApiResponse.Success(allItems)

        val viewModel = FeedViewModel(feedUseCase)
        advanceUntilIdle()

        val state = viewModel.state.value
        state.feed.shouldBeInstanceOf<LoadableDataState.Loaded<FeedsContainer>>()
        val loaded = state.feed as LoadableDataState.Loaded<FeedsContainer>
        loaded.data.featured shouldBe newsItems.take(7)
        loaded.data.items shouldBe newsItems
        loaded.data.articles shouldBe newsItems.drop(7)
        loaded.data.strategy shouldBe strategyItems

        verifySuspend { feedUseCase.getFeed() }
    }

    @Test
    fun init_sets_error_state_on_failure() = runTest(testDispatcher) {
        everySuspend { feedUseCase.getFeed() } returns ApiResponse.Error.NetworkError

        val viewModel = FeedViewModel(feedUseCase)
        advanceUntilIdle()

        viewModel.state.value.feed shouldBe LoadableDataState.Error

        verifySuspend { feedUseCase.getFeed() }
    }

    @Test
    fun dispatch_OnTabItemClick_updates_tabIndex() = runTest(testDispatcher) {
        everySuspend { feedUseCase.getFeed() } returns ApiResponse.Success(emptyList())

        val viewModel = FeedViewModel(feedUseCase)
        advanceUntilIdle()

        viewModel.state.value.tabIndex shouldBe 0

        viewModel.onDispatch(FeedAction.OnTabItemClick(1))
        advanceUntilIdle()

        viewModel.state.value.tabIndex shouldBe 1
    }

    @Test
    fun dispatch_Refresh_updates_state_and_clears_refreshing_flag() = runTest(testDispatcher) {
        val initialItems = listOf(createFeedItem("initial", NewsCategory.NEWS))
        val refreshedItems = listOf(
            createFeedItem("refreshed1", NewsCategory.NEWS),
            createFeedItem("refreshed2", NewsCategory.NEWS),
        )

        everySuspend { feedUseCase.getFeed() } returns ApiResponse.Success(initialItems)

        val viewModel = FeedViewModel(feedUseCase)
        advanceUntilIdle()

        everySuspend { feedUseCase.getFeed() } returns ApiResponse.Success(refreshedItems)

        viewModel.onDispatch(FeedAction.Refresh)
        advanceUntilIdle()

        val state = viewModel.state.value
        state.isRefreshing.shouldBeFalse()
        state.feed.shouldBeInstanceOf<LoadableDataState.Loaded<FeedsContainer>>()
        val loaded = state.feed as LoadableDataState.Loaded<FeedsContainer>
        loaded.data.items shouldBe refreshedItems
    }

    @Test
    fun dispatch_Refresh_clears_refreshing_flag_on_error() = runTest(testDispatcher) {
        val initialItems = listOf(createFeedItem("initial", NewsCategory.NEWS))

        everySuspend { feedUseCase.getFeed() } returns ApiResponse.Success(initialItems)

        val viewModel = FeedViewModel(feedUseCase)
        advanceUntilIdle()

        everySuspend { feedUseCase.getFeed() } returns ApiResponse.Error.NetworkError

        viewModel.onDispatch(FeedAction.Refresh)
        advanceUntilIdle()

        viewModel.state.value.isRefreshing.shouldBeFalse()
    }

    @Test
    fun selectedFeed_returns_articles_when_tabIndex_is_0() = runTest(testDispatcher) {
        val newsItems = List(10) { createFeedItem("news$it", NewsCategory.NEWS) }
        val strategyItems = listOf(createFeedItem("strategy1", NewsCategory.STRATEGY))

        everySuspend { feedUseCase.getFeed() } returns ApiResponse.Success(newsItems + strategyItems)

        val viewModel = FeedViewModel(feedUseCase)
        advanceUntilIdle()

        viewModel.state.value.tabIndex shouldBe 0
        viewModel.state.value.selectedFeed shouldBe newsItems.drop(7)
    }

    @Test
    fun selectedFeed_returns_strategy_when_tabIndex_is_1() = runTest(testDispatcher) {
        val newsItems = List(10) { createFeedItem("news$it", NewsCategory.NEWS) }
        val strategyItems = listOf(
            createFeedItem("strategy1", NewsCategory.STRATEGY),
            createFeedItem("strategy2", NewsCategory.STRATEGY),
        )

        everySuspend { feedUseCase.getFeed() } returns ApiResponse.Success(newsItems + strategyItems)

        val viewModel = FeedViewModel(feedUseCase)
        advanceUntilIdle()

        viewModel.onDispatch(FeedAction.OnTabItemClick(1))
        advanceUntilIdle()

        viewModel.state.value.selectedFeed shouldBe strategyItems
    }

    @Test
    fun selectedFeed_returns_empty_list_when_not_loaded() = runTest(testDispatcher) {
        everySuspend { feedUseCase.getFeed() } returns ApiResponse.Error.NetworkError

        val viewModel = FeedViewModel(feedUseCase)
        advanceUntilIdle()

        viewModel.state.value.selectedFeed.shouldBeEmpty()
    }

    private fun createFeedItem(id: String, category: NewsCategory): FeedItem {
        return FeedItem(
            link = "https://example.com/$id",
            image = "https://example.com/image/$id.jpg",
            title = "Title $id",
            pubDate = LocalDate(2024, 5, 10),
            category = category,
            site = "TestSite"
        )
    }
}
*/
