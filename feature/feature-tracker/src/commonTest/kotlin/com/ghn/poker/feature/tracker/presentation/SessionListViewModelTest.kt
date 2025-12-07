@file:Suppress("ktlint:standard:no-empty-file")

package com.ghn.poker.feature.tracker.presentation

// TODO: Re-enable once mokkery is updated for kotlin 2.3+ support
/*
import com.ghn.gizmodb.common.models.Venue
import com.ghn.poker.core.network.ApiResponse
import com.ghn.poker.core.testing.BaseViewModelTest
import com.ghn.poker.feature.tracker.domain.model.SessionData
import com.ghn.poker.feature.tracker.domain.usecase.SessionUseCase
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days

class SessionListViewModelTest : BaseViewModelTest() {

    private lateinit var sessionUseCase: SessionUseCase

    override fun onSetup() {
        sessionUseCase = mock<SessionUseCase>()
    }

    @Test
    fun init_fetches_sessions_and_updates_state_on_success() = runTest(testDispatcher) {
        val sessions = listOf(
            createSessionData("1", "500", "1200"),
            createSessionData("2", "1000", "800"),
        )

        everySuspend { sessionUseCase.getSessions() } returns ApiResponse.Success(sessions)

        val viewModel = SessionListViewModel(sessionUseCase)
        advanceUntilIdle()

        val state = viewModel.state.value
        state.sessions.shouldBeInstanceOf<LoadableDataState.Loaded<List<SessionData>>>()
        val loaded = state.sessions as LoadableDataState.Loaded<List<SessionData>>
        loaded.data shouldBe sessions

        verifySuspend { sessionUseCase.getSessions() }
    }

    @Test
    fun init_sets_empty_state_when_sessions_list_is_empty() = runTest(testDispatcher) {
        everySuspend { sessionUseCase.getSessions() } returns ApiResponse.Success(emptyList())

        val viewModel = SessionListViewModel(sessionUseCase)
        advanceUntilIdle()

        viewModel.state.value.sessions shouldBe LoadableDataState.Empty

        verifySuspend { sessionUseCase.getSessions() }
    }

    @Test
    fun init_sets_error_state_on_failure() = runTest(testDispatcher) {
        everySuspend { sessionUseCase.getSessions() } returns ApiResponse.Error.NetworkError

        val viewModel = SessionListViewModel(sessionUseCase)
        advanceUntilIdle()

        viewModel.state.value.sessions shouldBe LoadableDataState.Error

        verifySuspend { sessionUseCase.getSessions() }
    }

    @Test
    fun onDispatch_Retry_refetches_sessions() = runTest(testDispatcher) {
        val initialSessions = listOf(createSessionData("1", "500", "600"))
        val refreshedSessions = listOf(
            createSessionData("1", "500", "600"),
            createSessionData("2", "1000", "1500"),
        )

        everySuspend { sessionUseCase.getSessions() } returns ApiResponse.Success(initialSessions)

        val viewModel = SessionListViewModel(sessionUseCase)
        advanceUntilIdle()

        everySuspend { sessionUseCase.getSessions() } returns ApiResponse.Success(refreshedSessions)

        viewModel.onDispatch(SessionListAction.Retry)
        advanceUntilIdle()

        val state = viewModel.state.value
        state.sessions.shouldBeInstanceOf<LoadableDataState.Loaded<List<SessionData>>>()
        val loaded = state.sessions as LoadableDataState.Loaded<List<SessionData>>
        loaded.data shouldBe refreshedSessions
    }

    @Test
    fun onDispatch_Retry_sets_error_state_on_failure() = runTest(testDispatcher) {
        val initialSessions = listOf(createSessionData("1", "500", "600"))

        everySuspend { sessionUseCase.getSessions() } returns ApiResponse.Success(initialSessions)

        val viewModel = SessionListViewModel(sessionUseCase)
        advanceUntilIdle()

        everySuspend { sessionUseCase.getSessions() } returns ApiResponse.Error.NetworkError

        viewModel.onDispatch(SessionListAction.Retry)
        advanceUntilIdle()

        viewModel.state.value.sessions shouldBe LoadableDataState.Error
    }

    @Test
    fun loadableDataState_dataOrNull_returns_data_when_loaded() {
        val data = listOf("item1", "item2")
        val state: LoadableDataState<List<String>> = LoadableDataState.Loaded(data)

        state.dataOrNull shouldBe data // Not null case - keep shouldBe for value comparison
    }

    @Test
    fun loadableDataState_dataOrNull_returns_null_when_not_loaded() {
        val loadingState: LoadableDataState<List<String>> = LoadableDataState.Loading
        val errorState: LoadableDataState<List<String>> = LoadableDataState.Error
        val emptyState: LoadableDataState<List<String>> = LoadableDataState.Empty

        loadingState.dataOrNull.shouldBeNull()
        errorState.dataOrNull.shouldBeNull()
        emptyState.dataOrNull.shouldBeNull()
    }

    @Test
    fun loadableDataState_isLoading_returns_correct_value() {
        val loadingState: LoadableDataState<String> = LoadableDataState.Loading
        val loadedState: LoadableDataState<String> = LoadableDataState.Loaded("data")

        loadingState.isLoading.shouldBeTrue()
        loadedState.isLoading.shouldBeFalse()
    }

    @Test
    fun loadableDataState_isLoaded_returns_correct_value() {
        val loadedState: LoadableDataState<String> = LoadableDataState.Loaded("data")
        val loadingState: LoadableDataState<String> = LoadableDataState.Loading

        loadedState.isLoaded.shouldBeTrue()
        loadingState.isLoaded.shouldBeFalse()
    }

    private fun createSessionData(id: String, startAmount: String, endAmount: String): SessionData {
        return SessionData(
            date = Clock.System.now().minus(id.toLong().days),
            startAmount = startAmount,
            endAmount = endAmount,
            venue = Venue.MAGIC_CITY
        )
    }
}
*/
