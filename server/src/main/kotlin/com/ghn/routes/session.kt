package com.ghn.routes

import com.ghn.gizmodb.common.models.SessionDTO
import com.ghn.repository.SessionResponse
import com.ghn.service.SessionService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
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
import org.koin.ktor.ext.inject

private const val SESSIONS_ROUTE = "/sessions"

fun Routing.session() {
    val sessionService by inject<SessionService>()

    authenticate {
        route(SESSIONS_ROUTE) {
            get {
                val principal = call.principal<JWTPrincipal>()
                val id = principal?.payload?.getClaim("id")?.asInt() ?: run {
                    call.respond(HttpStatusCode.Unauthorized)
                    return@get
                }
                call.respond(sessionService.getUserSessions(id))
            }

            post {
                val principal = call.principal<JWTPrincipal>()
                val id = principal!!.payload.getClaim("id").asInt()
                val userSession = call.receive<SessionDTO>()
                sessionService.createSession(userSession, id)
                call.respond(HttpStatusCode.OK)
            }

            route("/{id}") {
                get {
                    val principal = call.principal<JWTPrincipal>()
                    val userId = principal!!.payload.getClaim("id").asInt()
                    val sessionId = call.parameters["id"].toString()

                    when (val response = sessionService.getUserSessionById(sessionId, userId)) {
                        is SessionResponse.Success -> call.respond(response.body)
                        is SessionResponse.Error.HttpError ->
                            call.respond(response.code, "Session not found for id: $sessionId")

                        SessionResponse.Error.NetworkError -> TODO()
                        SessionResponse.Error.SerializationError -> TODO()
                    }
                }

                delete {
                    val principal = call.principal<JWTPrincipal>()
                    val userId = principal!!.payload.getClaim("id").asInt()
                    val sessionId = call.parameters["id"].toString()
                    val result = sessionService.deleteSession(sessionId, userId)
                    call.respond(result)
                }
            }
        }
    }
}
