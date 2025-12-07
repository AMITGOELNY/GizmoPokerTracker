package com.ghn.poker.feature.feed.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.ghn.gizmodb.common.models.NewsCategory
import com.ghn.poker.core.network.ApiResponse
import com.ghn.poker.feature.feed.domain.model.FeedItem
import com.ghn.poker.feature.feed.domain.usecase.FeedUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

// TODO: Migrate to MviViewModel base class once Koin annotations (currently 2.3.3)
//  properly supports iOS targets with IR linking for classes inheriting from generic base classes.
@KoinViewModel
class FeedViewModel(
    private val feedUseCase: FeedUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(FeedState())
    val state = _state.asStateFlow()

    private val actions = MutableSharedFlow<FeedAction>(replay = 1)

    init {
        viewModelScope.launch {
            actions.collect { action ->
                Logger.d("FeedViewModel") { "Action: $action" }
                handleAction(action)
            }
        }
        onDispatch(FeedAction.Init)
    }

    fun onDispatch(action: FeedAction) {
        actions.tryEmit(action)
    }

    private suspend fun handleAction(action: FeedAction) {
        when (action) {
            FeedAction.Init -> getFeed()
            is FeedAction.OnTabItemClick -> _state.update { it.copy(tabIndex = action.index) }
            FeedAction.Refresh -> refresh()
        }
    }

    private suspend fun getFeed() {
        when (val result = feedUseCase.getFeed()) {
            is ApiResponse.Error -> {
                Logger.e { "Rss fetch failed $result" }
                _state.update { it.copy(feed = LoadableDataState.Error) }
            }

            is ApiResponse.Success -> {
                Logger.d { "Rss fetch success ${result.body}" }
                val group = result.body.groupBy { it.category }
                val articles = group.getOrElse(NewsCategory.NEWS) { emptyList() }
                val strategy = group.getOrElse(NewsCategory.STRATEGY) { emptyList() }
                _state.update {
                    it.copy(
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
        _state.update { it.copy(isRefreshing = true) }
        when (val result = feedUseCase.getFeed()) {
            is ApiResponse.Error -> {
                Logger.e { "Rss refresh failed $result" }
            }

            is ApiResponse.Success -> {
                Logger.d { "Rss refresh success ${result.body}" }
                val group = result.body.groupBy { it.category }
                val articles = group.getOrElse(NewsCategory.NEWS) { emptyList() }
                val strategy = group.getOrElse(NewsCategory.STRATEGY) { emptyList() }
                _state.update {
                    it.copy(
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
        _state.update { it.copy(isRefreshing = false) }
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
