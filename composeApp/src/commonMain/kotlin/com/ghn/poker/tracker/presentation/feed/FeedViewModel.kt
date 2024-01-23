package com.ghn.poker.tracker.presentation.feed

import co.touchlab.kermit.Logger
import com.ghn.gizmodb.common.models.NewsCategory
import com.ghn.poker.tracker.data.sources.remote.ApiResponse
import com.ghn.poker.tracker.domain.model.FeedItem
import com.ghn.poker.tracker.domain.usecase.FeedUseCase
import com.ghn.poker.tracker.presentation.BaseViewModel
import com.ghn.poker.tracker.presentation.session.LoadableDataState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FeedViewModel(
    private val feedUseCase: FeedUseCase
) : BaseViewModel() {
    private val _state = MutableStateFlow(FeedState())
    val state = _state.asStateFlow()

    private val viewStateTrigger = MutableSharedFlow<FeedActions>(replay = 1)

    init {
        viewModelScope.launch {
            viewStateTrigger.emit(FeedActions.Init)

            viewStateTrigger
                .onEach { Logger.d("FeedViewModel") { "action: $it" } }
                .collect { action ->
                    when (action) {
                        FeedActions.Init -> getFeed()
                        is FeedActions.OnTabItemClick ->
                            _state.update { it.copy(tabIndex = action.index) }
                    }
                }
        }
    }

    private suspend fun getFeed() {
        when (val result = feedUseCase.getFeed()) {
            is ApiResponse.Error -> {
                Logger.e { "Rss fetch failed $result" }
                _state.update { it.copy(feed = LoadableDataState.Error) }
            }

            is ApiResponse.Success -> _state.update { state ->
                Logger.d { "Rss fetch success ${result.body}" }
                val group = result.body.groupBy { it.category }
                val articles = group.getOrElse(NewsCategory.NEWS) { emptyList() }
                val strategy = group.getOrElse(NewsCategory.STRATEGY) { emptyList() }
                state.copy(
                    feed = LoadableDataState.Loaded(
                        FeedsContainer(
                            featured = articles.take(7),
                            items = articles,
                            articles = articles.subList(7, articles.lastIndex),
                            strategy = strategy
                        )
                    )
                )
            }
        }
    }

    fun dispatch(action: FeedActions) {
        viewStateTrigger.tryEmit(action)
    }
}

data class FeedState(
    val tabIndex: Int = 0,
    val feed: LoadableDataState<FeedsContainer> = LoadableDataState.Loading
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

sealed interface FeedActions {
    data object Init : FeedActions
    data class OnTabItemClick(val index: Int) : FeedActions
}
