package com.ghn.poker.core.preferences

import com.russhwolf.settings.ObservableSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

private fun <T> createStateFlow(
    key: String,
    defaultValue: T,
    getValue: (String, T) -> T,
    addListener: (String, T, (T) -> Unit) -> Unit,
): StateFlow<T> = MutableStateFlow(getValue(key, defaultValue)).apply {
    addListener(key, defaultValue) {
        tryEmit(it)
    }
}

private fun <T> createStateFlowNull(
    key: String,
    getValue: (String) -> T?,
    addListener: (String, (T?) -> Unit) -> Unit,
): StateFlow<T?> = MutableStateFlow(getValue(key)).apply {
    addListener(key) {
        tryEmit(it)
    }
}

fun ObservableSettings.boolAsFlow(key: String, defaultValue: Boolean): StateFlow<Boolean> =
    createStateFlow(key, defaultValue, ::getBoolean, ::addBooleanListener)

fun ObservableSettings.stringAsFlow(key: String): StateFlow<String?> =
    createStateFlowNull(key, ::getStringOrNull, ::addStringOrNullListener)
