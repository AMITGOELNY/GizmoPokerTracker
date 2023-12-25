package com.ghn.poker.tracker.di

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.ghn.poker.tracker.data.database.DataBaseDriver
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal actual val platformModule: Module = module {
    single { DataBaseDriver(get()).createDriver() }
    single {
        OkHttp.create {
//            preconfigured = okHttpClient
            config { retryOnConnectionFailure(true) }
        }
    }

    single<Settings>(named(ENCRYPTED_SETTINGS_NAME)) {
        SharedPreferencesSettings(
            delegate = EncryptedSharedPreferences.create(
                get(),
                ENCRYPTED_SETTINGS_NAME,
                MasterKey.Builder(get())
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build(),
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            ),
            commit = false
        )
    }

    single<ObservableSettings>(named(DEFAULT_SETTINGS_NAME)) {
        SharedPreferencesSettings(
            delegate = get<Context>().getSharedPreferences(
                DEFAULT_SETTINGS_NAME,
                Context.MODE_PRIVATE
            )
        )
    }
}
