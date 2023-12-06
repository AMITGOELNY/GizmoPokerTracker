package com.ghn.poker.tracker.presentation

import androidx.lifecycle.ViewModel as AndroidXViewModel
import androidx.lifecycle.viewModelScope as androidXViewModelScope

actual abstract class BaseViewModel : AndroidXViewModel() {
    actual val viewModelScope = androidXViewModelScope

    actual override fun onCleared() = Unit
}
