package com.ghn.poker.tracker.presentation

import kotlinx.coroutines.CoroutineScope

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect abstract class BaseViewModel() {
    val viewModelScope: CoroutineScope

    /**
     * This method will be called when this ViewModel is no longer used and will be destroyed.
     * It is useful when ViewModel observes some data and you need to clear this subscription
     * to prevent a leak of this ViewModel.
     */
    protected open fun onCleared()
}
