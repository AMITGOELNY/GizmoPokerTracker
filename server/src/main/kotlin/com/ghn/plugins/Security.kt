package com.ghn.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.application.log
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.server.sessions.get
import io.ktor.server.sessions.sessions
import io.ktor.server.sessions.set

fun Application.configureSecurity() {
    data class MySession(val count: Int = 0)
//    install(Sessions) {
//        cookie<MySession>("MY_SESSION") {
//            cookie.extensions["SameSite"] = "lax"
//        }
//    }
    install(Authentication) {
        jwt {
            verifier(JwtConfig.verifier)
            realm = "com.ghn"
            validate { credential ->
                val name = credential.payload.getClaim("username").asString()
                val id = credential.payload.getClaim("id").asInt()
                this@configureSecurity.log.debug("id: $id, name: $name")
                JWTPrincipal(credential.payload).takeIf { name != null && id != null }
            }
        }
    }

    routing {
        get("/session/increment") {
            val session = call.sessions.get<MySession>() ?: MySession()
            call.sessions.set(session.copy(count = session.count + 1))
            call.respondText("Counter is ${session.count}. Refresh to increment.")
        }
    }
}
