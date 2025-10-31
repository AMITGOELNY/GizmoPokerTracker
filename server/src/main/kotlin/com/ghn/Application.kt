package com.ghn

import com.ghn.di.ClientModule
import com.ghn.di.RepositoryModule
import com.ghn.di.ServiceModule
import com.ghn.plugins.JwtConfig
import com.ghn.plugins.configureHTTP
import com.ghn.plugins.configureInfoFetch
import com.ghn.plugins.configureMonitoring
import com.ghn.plugins.configureRouting
import com.ghn.plugins.configureSecurity
import com.ghn.plugins.configureSerialization
import com.ghn.service.EvaluatorService
import com.ghn.service.EvaluatorServiceImpl
import com.prof18.rssparser.RssParser
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.netty.EngineMain
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.FlywayException
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.koin.dsl.module
import org.koin.ksp.generated.module
import org.koin.ktor.plugin.Koin
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.sqlite.javax.SQLiteConnectionPoolDataSource

fun main(args: Array<String>) {
    EngineMain.main(args)
//    embeddedServer(Netty, port = 8080, configure = {
//        connectionGroupSize = 2
//        workerGroupSize = 9
//        callGroupSize = 200
//        shutdownGracePeriod = 2000
//        shutdownTimeout = 3000
//    }) {
//        // ...
//    }.start(wait = true)
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

    install(Koin) {
        modules(
            ServiceModule().module,
            RepositoryModule().module,
            ClientModule().module
        )
        modules(
            module {
                val source = SQLiteConnectionPoolDataSource().apply {
                    url = dbUrl
                    check(url.isNotBlank())
                }

                single { DSL.using(source, SQLDialect.SQLITE) }
                single { RssParser() }
                single<EvaluatorService> { EvaluatorServiceImpl(LoggerFactory.getLogger("EvaluatorService")) }
            }
        )
    }
    configureSerialization()
    configureMonitoring()
    configureHTTP()
    configureSecurity()
    configureRouting()
    configureInfoFetch()
}
