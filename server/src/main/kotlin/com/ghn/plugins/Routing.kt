package com.ghn.plugins

import com.ghn.routes.auth
import com.ghn.routes.evaluator
import com.ghn.routes.feed
import com.ghn.routes.session
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.plugins.autohead.AutoHeadResponse
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun Application.configureRouting() {
    install(AutoHeadResponse)
    routing {
        get("/") { call.respondText("Hello World!") }
        auth()
        session()
        feed()
        evaluator()
    }
}
