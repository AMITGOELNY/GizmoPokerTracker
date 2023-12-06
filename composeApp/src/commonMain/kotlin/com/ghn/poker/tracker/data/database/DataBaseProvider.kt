package com.ghn.poker.tracker.data.database

import app.cash.sqldelight.db.SqlDriver
import com.ghn.poker.tracker.GizmoPokerDb

internal const val DB_NAME = "GizmoPokerDb"

class DatabaseProvider(sqlDriver: SqlDriver) {
    val database: GizmoPokerDb = GizmoPokerDb(
        driver = sqlDriver
    )
}

internal expect class DataBaseDriver {
    fun createDriver(): SqlDriver
}
