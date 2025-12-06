package com.ghn.poker.core.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver

internal actual class DataBaseDriver {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(GizmoPokerDb.Schema, DB_NAME)
    }
}
