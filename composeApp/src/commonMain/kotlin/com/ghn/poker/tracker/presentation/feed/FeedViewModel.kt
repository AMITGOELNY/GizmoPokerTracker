package com.ghn.poker.tracker.presentation.feed

import co.touchlab.kermit.Logger
import com.ghn.poker.tracker.data.sources.remote.ApiResponse
import com.ghn.poker.tracker.domain.model.FeedItem
import com.ghn.poker.tracker.domain.usecase.FeedUseCase
import com.ghn.poker.tracker.presentation.BaseViewModel
import com.ghn.poker.tracker.presentation.session.LoadableDataState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
            getFeed()
        }
    }

    private suspend fun getFeed() {
        when (val result = feedUseCase.getFeed()) {
            is ApiResponse.Error -> {
                Logger.e { "Rss fetch failed $result" }
                _state.update { it.copy(feed = LoadableDataState.Error) }
            }

            is ApiResponse.Success -> _state.update {
                Logger.d { "Rss fetch success ${result.body}" }
                it.copy(
                    feed = LoadableDataState.Loaded(
                        FeedsContainer(
                            featured = result.body.take(7),
                            items = result.body.subList(7, result.body.lastIndex)
                        )
                    )
                )
            }
        }
    }
}

data class FeedState(
    val feed: LoadableDataState<FeedsContainer> = LoadableDataState.Loading
)

data class FeedsContainer(
    val featured: List<FeedItem>,
    val items: List<FeedItem>,
)

sealed interface FeedActions {
    data object Init : FeedActions
}
