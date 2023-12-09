package com.ghn.plugins

import com.ghn.common.models.SessionDTO
import com.ghn.gizmodb.tables.pojos.SessionDb
import com.ghn.gizmodb.tables.references.SESSION
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.plugins.autohead.AutoHeadResponse
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import org.jooq.DSLContext
import java.time.LocalDateTime

fun Application.configureRouting(db: DSLContext) {
    install(AutoHeadResponse)
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        sessionRouting(db)
    }
}

fun Route.sessionRouting(db: DSLContext) {
    route("/sessions") {
        get {
            val sessionsDb = db.fetch(SESSION).into(SessionDb::class.java)
            val sessions = sessionsDb.map(SessionDb::toSessions)
            call.respond(sessions)
        }

        post {
            val userSession = call.receive<SessionDTO>()
            val newRecord = db.newRecord(SESSION)
            newRecord.from(userSession.toSessionDb())
            newRecord.store()
            call.respond(HttpStatusCode.OK)
        }
    }
}

private fun SessionDb.toSessions() =
    SessionDTO(
        id = id,
        date = date.toString(),
        startAmount = startamount,
        endAmount = endamount,
    )

private fun SessionDTO.toSessionDb() =
    SessionDb(
        id = id,
        date = LocalDateTime.now(),
        startamount = startAmount,
        endamount = endAmount,
    )
