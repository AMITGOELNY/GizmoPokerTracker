package com.ghn

import com.ghn.plugins.*
import io.ktor.server.application.*
import io.ktor.server.netty.*
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.FlywayException
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.slf4j.Logger
import org.sqlite.javax.SQLiteConnectionPoolDataSource

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun runMigrations(
    connectionString: String,
    logger: Logger? = null
): Boolean {
    val flyway = Flyway.configure()
        .loggers("slf4j")
        .dataSource(connectionString, null, null).load()
    return try {
        flyway.migrate()
        true
    } catch (e: FlywayException) {
        logger?.error("Database migrations failed", e)
        false
    }
}

fun Application.module() {
    val secret = environment.config.property("ktor.SECRET_JWT").getString()
//    environment.developmentMode
    JwtConfig.initialize(secret)
    val dbUrl = "jdbc:${environment.config.property("ktor.databaseUrl").getString()}"
    runMigrations(dbUrl)
    val source = SQLiteConnectionPoolDataSource().apply {
        url = dbUrl
        check(url.isNotBlank())
    }

    val db = DSL.using(source, SQLDialect.SQLITE)
    configureSerialization()
    configureMonitoring()
    configureHTTP()
    configureSecurity()
    configureRouting(db)
}
