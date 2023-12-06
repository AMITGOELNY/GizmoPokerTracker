package com.ghn.poker.tracker.data.database

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.ghn.poker.tracker.GizmoPokerDb

internal actual class DataBaseDriver(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(GizmoPokerDb.Schema, context, DB_NAME)
    }
}
