package com.ghn.poker.core.preferences

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
