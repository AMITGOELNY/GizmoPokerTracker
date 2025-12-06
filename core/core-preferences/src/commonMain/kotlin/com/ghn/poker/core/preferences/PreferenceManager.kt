package com.ghn.poker.core.preferences

import kotlinx.coroutines.flow.StateFlow
import kotlin.reflect.KClass

/**
 * Preference manager to facilitate access to platform specific
 * settings instances to manage persisted KV pairs.
 */
interface PreferenceManager {
    fun clearPrefs()
    val count: Int

    val tokenFlow: StateFlow<String?>

    fun <T : Comparable<T>> getPreference(preferenceKey: PreferenceKey, kClass: KClass<T>): T?
    fun <T : Comparable<T>> setPreference(preferenceKey: PreferenceKey, value: T, kClass: KClass<T>)
}

inline operator fun <reified T : Comparable<T>> PreferenceManager.get(key: String): T? =
    getPreference(PreferenceKey(key), T::class)

inline operator fun <reified T : Comparable<T>> PreferenceManager.set(
    key: String,
    value: T
) = setPreference(PreferenceKey(key), value, T::class)
