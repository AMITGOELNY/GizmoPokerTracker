package com.ghn.poker.tracker.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.ghn.poker.core.database.GizmoPokerDb
import com.ghn.poker.core.preferences.DEFAULT_SETTINGS_NAME
import com.ghn.poker.core.preferences.ENCRYPTED_SETTINGS_NAME
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.PreferencesSettings
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.util.prefs.Preferences

private const val DB_NAME = "GizmoPokerDb.db"

internal actual val platformModule: Module = module {
    single<SqlDriver> {
        JdbcSqliteDriver("jdbc:sqlite:$DB_NAME").also {
            GizmoPokerDb.Schema.create(it)
        }
    }

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
