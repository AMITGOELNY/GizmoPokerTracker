package com.ghn.plugins

import com.ghn.common.models.SessionDTO
import com.ghn.gizmodb.tables.pojos.SessionDb
import com.ghn.gizmodb.tables.references.SESSION
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
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
            val sessions = sessionsDb.map(SessionDb::toSession)
            call.respond(sessions)
        }

        post {
            val userSession = call.receive<SessionDTO>()
            val newRecord = db.newRecord(SESSION)
            newRecord.from(userSession.toSessionDb())
            newRecord.store()
            call.respond(HttpStatusCode.OK)
        }

        route("/{id}") {
            get {
                val id = call.parameters["id"].toString()
                val session =
                    db.selectFrom(SESSION)
                        .where(SESSION.ID.eq(id))
                        .fetch()
                        .firstOrNull()
                        ?.into(SessionDb::class.java)
                if (session != null) {
                    call.respond(session.toSession())
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Session not found for id: $id")
                }
            }

            delete {
                val id = call.parameters["id"].toString()
                try {
                    val success =
                        db.deleteFrom(SESSION)
                            .where(SESSION.ID.eq(id))
                            .execute()
                    if (success != 0) {
                        call.respond(HttpStatusCode.OK)
                    } else {
                        call.respond(HttpStatusCode.NotAcceptable)
                    }
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, "Delete session failed for id: $id")
                }
            }
        }
    }
}

private fun SessionDb.toSession() =
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
