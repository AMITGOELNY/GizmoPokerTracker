package com.ghn.repository

import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.sqlite.SQLiteDataSource
import java.sql.Connection

/**
 * Base class for repository integration tests.
 * Sets up an in-memory SQLite database with schema migrations for each test.
 */
abstract class RepositoryTestBase {

    protected lateinit var db: DSLContext
    private lateinit var dataSource: SQLiteDataSource
    private lateinit var connection: Connection

    @BeforeEach
    fun setupDatabase() {
        // Create in-memory SQLite database
        dataSource = SQLiteDataSource().apply {
            url = "jdbc:sqlite::memory:"
        }

        // Get a connection and keep it open for the duration of the test
        // This ensures the in-memory database persists
        connection = dataSource.connection

        // Execute migration SQL directly on this connection
        // We can't use Flyway because it creates its own connections, and SQLite in-memory databases are per-connection
        val migrations = listOf("V1__Create_database.sql", "V2__Create_refresh_token_table.sql")

        migrations.forEach { migrationFile ->
            val migrationSql = javaClass.classLoader.getResourceAsStream("db/migration/$migrationFile")
                ?.bufferedReader()
                ?.use { it.readText() }
                ?: throw IllegalStateException("Could not find migration file $migrationFile")

            val statement = connection.createStatement()
            statement.executeUpdate(migrationSql)
            statement.close()
        }

        // Create jOOQ DSLContext using the same connection
        db = DSL.using(connection, SQLDialect.SQLITE)
    }

    @AfterEach
    fun tearDownDatabase() {
        // Close the connection to cleanup the in-memory database
        if (::connection.isInitialized && !connection.isClosed) {
            connection.close()
        }
    }

    /**
     * Helper method to clear all data from tables between tests if needed.
     * Useful for tests that need a fresh database state.
     */
    protected fun clearAllTables() {
        db.execute("DELETE FROM refresh_token")
        db.execute("DELETE FROM session")
        db.execute("DELETE FROM user")
    }
}
