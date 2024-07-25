package com.ghn.routes

import com.ghn.gizmodb.common.models.FeedDTO
import com.ghn.gizmodb.tables.references.FEED
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import org.jooq.DSLContext
import org.koin.ktor.ext.inject

internal fun Routing.feed() {
    val db by inject<DSLContext>()
    route("/feed") {
        get {
            val feed = db.fetch(FEED).into(FeedDTO::class.java)
            call.respond(feed)
        }
    }
}
