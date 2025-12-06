package com.ghn.poker.core.preferences

import com.russhwolf.settings.ObservableSettings
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import kotlin.reflect.KClass

const val ENCRYPTED_SETTINGS_NAME = "ENCRYPTED_SETTINGS"
const val DEFAULT_SETTINGS_NAME = "DEFAULT_SETTINGS"

@Single([PreferenceManager::class])
class PrefsManager(
    @Named(ENCRYPTED_SETTINGS_NAME) private val encryptedPrefs: ObservableSettings,
    @Named(DEFAULT_SETTINGS_NAME) private val defaultPrefs: ObservableSettings
) : PreferenceManager {

    private fun getSettings(key: PreferenceKey) = encryptedPrefs

    override fun clearPrefs() = encryptedPrefs.clear()

    override val count: Int get() = encryptedPrefs.size

    override val tokenFlow: StateFlow<String?> = encryptedPrefs
        .stringAsFlow(Preferences.USER_TOKEN_KEY)

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
