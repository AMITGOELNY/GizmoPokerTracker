package com.ghn.poker.tracker.data.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.ghn.poker.tracker.GizmoPokerDb

internal actual class DataBaseDriver {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(GizmoPokerDb.Schema, DB_NAME)
    }
}
