package com.ghn.poker.tracker.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.ghn.poker.core.database.GizmoPokerDb
import com.ghn.poker.core.preferences.DEFAULT_SETTINGS_NAME
import com.ghn.poker.core.preferences.ENCRYPTED_SETTINGS_NAME
import com.ghn.poker.core.preferences.asObservableSettings
import com.russhwolf.settings.ExperimentalSettingsImplementation
import com.russhwolf.settings.KeychainSettings
import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.ObservableSettings
import io.ktor.client.engine.darwin.Darwin
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import platform.Foundation.NSUserDefaults

private const val DB_NAME = "GizmoPokerDb"

@OptIn(ExperimentalSettingsImplementation::class)
internal actual val platformModule: Module = module {
    single<SqlDriver> { NativeSqliteDriver(GizmoPokerDb.Schema, DB_NAME) }
    single { Darwin.create { } }

    single<ObservableSettings>(named(ENCRYPTED_SETTINGS_NAME)) {
        KeychainSettings(service = ENCRYPTED_SETTINGS_NAME).asObservableSettings()
    }

    single<ObservableSettings>(named(DEFAULT_SETTINGS_NAME)) {
        NSUserDefaultsSettings(NSUserDefaults(suiteName = DEFAULT_SETTINGS_NAME))
    }
}
