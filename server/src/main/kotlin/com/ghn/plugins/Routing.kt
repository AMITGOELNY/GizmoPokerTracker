package com.ghn.plugins

import com.ghn.gizmodb.tables.pojos.SessionDb
import com.ghn.gizmodb.tables.references.SESSION
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
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
            val userSession = call.receive<Session>()
            val newRecord = db.newRecord(SESSION)
            newRecord.from(userSession.toSessionDb())
            newRecord.store()
            call.respond(HttpStatusCode.OK)
        }
    }
}

private fun SessionDb.toSessions() =
    Session(
        id = id,
        date = date.toString(),
        startAmount = startamount,
        endAmount = endamount,
    )

private fun Session.toSessionDb() =
    SessionDb(
        id = id,
        date = LocalDateTime.now(),
        startamount = startAmount,
        endamount = endAmount,
    )

@Serializable
data class Session(
    val id: String,
    val date: String,
    val startAmount: String?,
    val endAmount: String?,
)
