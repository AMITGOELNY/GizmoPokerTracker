package com.ghn.poker.feature.feed.presentation

import co.touchlab.kermit.Logger
import com.ghn.gizmodb.common.models.NewsCategory
import com.ghn.poker.core.common.presentation.MviViewModel
import com.ghn.poker.core.network.ApiResponse
import com.ghn.poker.feature.feed.domain.model.FeedItem
import com.ghn.poker.feature.feed.domain.usecase.FeedUseCase
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class FeedViewModel(
    private val feedUseCase: FeedUseCase
) : MviViewModel<FeedState, FeedAction, FeedEffect>() {

    override val initialState = FeedState()

    init {
        onDispatch(FeedAction.Init)
    }

    override suspend fun handleAction(action: FeedAction) {
        when (action) {
            FeedAction.Init -> getFeed()
            is FeedAction.OnTabItemClick -> updateState { copy(tabIndex = action.index) }
            FeedAction.Refresh -> refresh()
        }
    }

    private suspend fun getFeed() {
        when (val result = feedUseCase.getFeed()) {
            is ApiResponse.Error -> {
                Logger.e { "Rss fetch failed $result" }
                updateState { copy(feed = LoadableDataState.Error) }
            }

            is ApiResponse.Success -> {
                Logger.d { "Rss fetch success ${result.body}" }
                val group = result.body.groupBy { it.category }
                val articles = group.getOrElse(NewsCategory.NEWS) { emptyList() }
                val strategy = group.getOrElse(NewsCategory.STRATEGY) { emptyList() }
                updateState {
                    copy(
                        feed = LoadableDataState.Loaded(
                            FeedsContainer(
                                featured = articles.take(7),
                                items = articles,
                                articles = articles.drop(7),
                                strategy = strategy
                            )
                        )
                    )
                }
            }
        }
    }

    private suspend fun refresh() {
        updateState { copy(isRefreshing = true) }
        when (val result = feedUseCase.getFeed()) {
            is ApiResponse.Error -> {
                Logger.e { "Rss refresh failed $result" }
            }

            is ApiResponse.Success -> {
                Logger.d { "Rss refresh success ${result.body}" }
                val group = result.body.groupBy { it.category }
                val articles = group.getOrElse(NewsCategory.NEWS) { emptyList() }
                val strategy = group.getOrElse(NewsCategory.STRATEGY) { emptyList() }
                updateState {
                    copy(
                        feed = LoadableDataState.Loaded(
                            FeedsContainer(
                                featured = articles.take(7),
                                items = articles,
                                articles = articles.drop(7),
                                strategy = strategy
                            )
                        )
                    )
                }
            }
        }
        updateState { copy(isRefreshing = false) }
    }
}

data class FeedState(
    val tabIndex: Int = 0,
    val feed: LoadableDataState<FeedsContainer> = LoadableDataState.Loading,
    val isRefreshing: Boolean = false
) {
    val selectedFeed: List<FeedItem>
        get() {
            if (feed !is LoadableDataState.Loaded) return emptyList()
            return if (tabIndex == 0) feed.data.articles else feed.data.strategy
        }
}

data class FeedsContainer(
    val featured: List<FeedItem>,
    val items: List<FeedItem>,
    val articles: List<FeedItem>,
    val strategy: List<FeedItem>,
)

sealed interface FeedAction {
    data object Init : FeedAction
    data class OnTabItemClick(val index: Int) : FeedAction
    data object Refresh : FeedAction
}

sealed interface FeedEffect

sealed class LoadableDataState<out T> {
    data object Loading : LoadableDataState<Nothing>()

    data object Empty : LoadableDataState<Nothing>()

    data object Error : LoadableDataState<Nothing>()

    data class Loaded<out T>(val data: T) : LoadableDataState<T>()

    val dataOrNull: T?
        get() = (this as? Loaded<T>)?.data

    val isLoading: Boolean get() = this is Loading
    val isLoaded: Boolean get() = this is Loaded
}
