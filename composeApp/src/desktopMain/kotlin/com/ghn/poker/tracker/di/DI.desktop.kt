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
import java.io.File
import java.util.prefs.Preferences

private const val DB_NAME = "GizmoPokerDb.db"

internal actual val platformModule: Module = module {
    single<SqlDriver> {
        val dbFile = File(DB_NAME)
        val dbExists = dbFile.exists()
        JdbcSqliteDriver("jdbc:sqlite:$DB_NAME").also { driver ->
            if (!dbExists) {
                GizmoPokerDb.Schema.create(driver)
            }
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
