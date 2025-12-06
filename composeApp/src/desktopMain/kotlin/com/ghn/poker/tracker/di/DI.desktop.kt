package com.ghn.poker.tracker.di

import com.ghn.poker.core.preferences.DEFAULT_SETTINGS_NAME
import com.ghn.poker.core.preferences.ENCRYPTED_SETTINGS_NAME
import com.ghn.poker.tracker.data.database.DataBaseDriver
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.PreferencesSettings
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.util.prefs.Preferences

internal actual val platformModule: Module = module {
    single { DataBaseDriver().createDriver() }

    single {
        OkHttp.create {
            config { retryOnConnectionFailure(true) }
        }
    }

    single<ObservableSettings>(named(ENCRYPTED_SETTINGS_NAME)) {
        PreferencesSettings(Preferences.userRoot())
    }

    single<ObservableSettings>(named(DEFAULT_SETTINGS_NAME)) {
        PreferencesSettings(Preferences.userRoot())
    }

}
