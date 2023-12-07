package com.ghn.poker.tracker.presentation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

/**
 * Base class that provides a jvm to the AndroidX `ViewModel`.
 * In particular, this provides= a [CoroutineScope][kotlinx.coroutines.CoroutineScope]
 * which uses [Dispatchers.Main][kotlinx.coroutines.Dispatchers.Main] and can be tied
 * into an arbitrary lifecycle by calling [clear] at the appropriate time.
 */
actual abstract class BaseViewModel {

    actual val viewModelScope: CoroutineScope = CoroutineScope( SupervisorJob() + Dispatchers.IO)

    /**
     * Override this to do any cleanup immediately before the internal
     * [CoroutineScope][kotlinx.coroutines.CoroutineScope]
     * is cancelled in [clear]
     */
    protected actual open fun onCleared() {
    }

    /**
     * Cancels the internal [CoroutineScope][kotlinx.coroutines.CoroutineScope].
     * After this is called, the ViewModel should no longer be used.
     */
    private fun clear() {
        onCleared()
        viewModelScope.cancel()
    }
}