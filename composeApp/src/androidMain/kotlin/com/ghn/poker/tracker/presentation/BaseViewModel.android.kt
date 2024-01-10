package com.ghn.poker.tracker.presentation

import androidx.lifecycle.ViewModel as AndroidXViewModel
import androidx.lifecycle.viewModelScope as androidXViewModelScope

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual abstract class BaseViewModel : AndroidXViewModel() {
    actual val viewModelScope = androidXViewModelScope

    actual override fun onCleared() = Unit
}
