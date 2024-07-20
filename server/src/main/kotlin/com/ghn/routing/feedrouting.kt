package com.ghn.routing

import com.ghn.gizmodb.common.models.FeedDTO
import com.ghn.gizmodb.tables.references.FEED
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import org.jooq.DSLContext

internal fun Routing.feedRouting(db: DSLContext) {
    route("/feed") {
        get {
            val feed = db.fetch(FEED).into(FeedDTO::class.java)
            call.respond(feed)
        }
    }
}