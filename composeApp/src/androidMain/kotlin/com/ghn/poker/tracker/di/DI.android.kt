package com.ghn.poker.tracker.di

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.ghn.poker.core.database.GizmoPokerDb
import com.ghn.poker.core.preferences.DEFAULT_SETTINGS_NAME
import com.ghn.poker.core.preferences.ENCRYPTED_SETTINGS_NAME
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.SharedPreferencesSettings
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

private const val DB_NAME = "GizmoPokerDb"

internal actual val platformModule: Module = module {
    single<SqlDriver> { AndroidSqliteDriver(GizmoPokerDb.Schema, get(), DB_NAME) }
    single {
        OkHttp.create {
//            preconfigured = okHttpClient
            config { retryOnConnectionFailure(true) }
        }
    }

    single<ObservableSettings>(named(ENCRYPTED_SETTINGS_NAME)) {
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
