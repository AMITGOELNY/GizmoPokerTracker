package com.ghn.poker.tracker.di

import com.ghn.poker.tracker.data.database.DataBaseDriver
import com.russhwolf.settings.KeychainSettings
import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import io.ktor.client.engine.darwin.*
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import platform.Foundation.NSUserDefaults

internal actual val platformModule: Module = module {
    single { DataBaseDriver().createDriver() }
    single { Darwin.create { } }

    single<Settings>(named(ENCRYPTED_SETTINGS_NAME)) {
        KeychainSettings(service = ENCRYPTED_SETTINGS_NAME)
    }

    single<ObservableSettings>(named(DEFAULT_SETTINGS_NAME)) {
        NSUserDefaultsSettings(NSUserDefaults(suiteName = DEFAULT_SETTINGS_NAME))
    }
}
