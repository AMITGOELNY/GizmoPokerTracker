package com.ghn.routing

import com.ghn.gizmodb.common.models.SessionDTO
import com.ghn.gizmodb.tables.references.SESSION
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.jooq.DSLContext

fun Routing.sessionRouting(db: DSLContext) {
    authenticate {
        route("/sessions") {
            get {
                val principal = call.principal<JWTPrincipal>()
                val id = principal!!.payload.getClaim("id").asInt()
                val sessions = db.fetch(SESSION, SESSION.USERID.eq(id)).into(SessionDTO::class.java)
                call.respond(sessions)
            }

            post {
                val principal = call.principal<JWTPrincipal>()
                val id = principal!!.payload.getClaim("id").asInt()
                val userSession = call.receive<SessionDTO>()

                val newRecord = db.newRecord(SESSION)
                newRecord.from(userSession)
                newRecord.userid = id
                newRecord.store()

                call.respond(HttpStatusCode.OK)
            }

            route("/{id}") {
                get {
                    val principal = call.principal<JWTPrincipal>()
                    val userId = principal!!.payload.getClaim("id").asInt()
                    val id = call.parameters["id"].toString()
                    val session = db.fetchOne(SESSION, SESSION.ID.eq(id), SESSION.USERID.eq(userId))
                        ?.into(SessionDTO::class.java)
                    if (session != null) {
                        call.respond(session)
                    } else {
                        call.respond(HttpStatusCode.BadRequest, "Session not found for id: $id")
                    }
                }

                delete {
                    val principal = call.principal<JWTPrincipal>()
                    val userId = principal!!.payload.getClaim("id").asInt()
                    val id = call.parameters["id"].toString()
                    try {
                        val success = db.deleteFrom(SESSION)
                            .where(SESSION.ID.eq(id), SESSION.USERID.eq(userId))
                            .execute()
                        if (success != 0) {
                            call.respond(HttpStatusCode.OK)
                        } else {
                            call.respond(HttpStatusCode.NotAcceptable)
                        }
                    } catch (e: Exception) {
                        call.respond(
                            HttpStatusCode.InternalServerError,
                            "Delete session failed for id: $id"
                        )
                    }
                }
            }
        }
    }
}
