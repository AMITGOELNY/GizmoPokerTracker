package com.ghn.poker.tracker.data.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.ghn.poker.tracker.GizmoPokerDb

internal actual class DataBaseDriver {
    actual fun createDriver(): SqlDriver {
        val driver: SqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        GizmoPokerDb.Schema.create(driver)
        return driver
    }
}
