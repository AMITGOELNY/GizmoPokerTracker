package com.ghn.poker.tracker.data.preferences

import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import kotlin.jvm.JvmInline
import kotlin.reflect.KClass

/**
 * Preference manager to facilitate access to platform specific
 * settings instances to manage persisted KV pairs.
 */
internal class PrefsManager(
    private val encryptedPrefs: Settings,
    private val defaultPrefs: ObservableSettings
) : PreferenceManager {

    private fun getSettings(key: PreferenceKey) = encryptedPrefs
//        if (key.isEncrypted) encryptedPrefs else defaultPrefs

    override fun clearPrefs() = encryptedPrefs.clear()

    override val count: Int get() = encryptedPrefs.size

    @Suppress("UNCHECKED_CAST")
    override fun <T : Comparable<T>> getPreference(
        preferenceKey: PreferenceKey,
        kClass: KClass<T>
    ): T? {
        val preference = getSettings(preferenceKey)
        return when (kClass) {
            String::class -> preference.getStringOrNull(preferenceKey.key) as? T?
            Int::class -> preference.getIntOrNull(preferenceKey.key) as? T?
            Boolean::class -> preference.getBooleanOrNull(preferenceKey.key) as? T?
            Float::class -> preference.getFloatOrNull(preferenceKey.key) as? T?
            Long::class -> preference.getLongOrNull(preferenceKey.key) as? T?
            else -> throw UnsupportedOperationException("Type $kClass is not supported yet")
        }
    }

    override fun <T : Comparable<T>> setPreference(
        preferenceKey: PreferenceKey,
        value: T,
        kClass: KClass<T>
    ) {
        val preference = getSettings(preferenceKey)
        when (kClass) {
            String::class -> preference.putString(preferenceKey.key, value as String)
            Int::class -> preference.putInt(preferenceKey.key, value as Int)
            Boolean::class -> preference.putBoolean(preferenceKey.key, value as Boolean)
            Float::class -> preference.putFloat(preferenceKey.key, value as Float)
            Long::class -> preference.putLong(preferenceKey.key, value as Long)
            else -> throw UnsupportedOperationException("Type $kClass is not supported yet")
        }
    }
}

internal interface PreferenceManager {
    fun clearPrefs()
    val count: Int

    fun <T : Comparable<T>> getPreference(preferenceKey: PreferenceKey, kClass: KClass<T>): T?
    fun <T : Comparable<T>> setPreference(preferenceKey: PreferenceKey, value: T, kClass: KClass<T>)
}

internal inline operator fun <reified T : Comparable<T>> PreferenceManager.get(key: String): T? =
    getPreference(PreferenceKey(key), T::class)

internal inline operator fun <reified T : Comparable<T>> PreferenceManager.set(
    key: String,
    value: T
) = setPreference(PreferenceKey(key), value, T::class)

object Preferences {
    const val USER_ID = "LOGGED_IN_USER_ID"
    const val USER_TOKEN_KEY = "USER_TOKEN"
    const val REFRESH_TOKEN_KEY = "REFRESH_TOKEN"
    const val REFRESH_TOKEN_EXPIRED_KEY = "REFRESH_TOKEN_EXPIRED"

    private val encryptedKeys = listOf(
        USER_ID,
        USER_TOKEN_KEY,
        REFRESH_TOKEN_KEY,
        REFRESH_TOKEN_EXPIRED_KEY
    )

    val PreferenceKey.isEncrypted get() = encryptedKeys.contains(this.key)
}

@JvmInline
value class PreferenceKey(val key: String)
