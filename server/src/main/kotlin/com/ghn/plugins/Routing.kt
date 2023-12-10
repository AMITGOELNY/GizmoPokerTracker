package com.ghn.plugins

import com.ghn.gizmodb.common.models.SessionDTO
import com.ghn.gizmodb.tables.references.SESSION
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jooq.DSLContext

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
            val sessions = db.fetch(SESSION).into(SessionDTO::class.java)
            call.respond(sessions)
        }

        post {
            val userSession = call.receive<SessionDTO>()
            val newRecord = db.newRecord(SESSION)
            newRecord.from(userSession)
            newRecord.store()
            call.respond(HttpStatusCode.OK)
        }

        route("/{id}") {
            get {
                val id = call.parameters["id"].toString()
                val session =
                    db.fetchOne(SESSION, SESSION.ID.eq(id))
                        ?.into(SessionDTO::class.java)
                if (session != null) {
                    call.respond(session)
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
