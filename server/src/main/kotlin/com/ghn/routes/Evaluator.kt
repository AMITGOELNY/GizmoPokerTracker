package com.ghn.routes

import com.ghn.gizmodb.common.models.Card
import com.ghn.gizmodb.common.models.EvaluatorRequest
import com.ghn.gizmodb.evaluator.models.Evaluator
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.post
import io.ktor.server.routing.route

internal fun Routing.evaluator() {
    route("/evaluator") {
        post {
            val log = call.application.environment.log
            val request = call.receive<EvaluatorRequest>()
            val (hero, board, villain, simulationCount) = request
            val evaluator = Evaluator { }
            val results = evaluator.processCards(hero, villain, board, simulationCount)
            call.respond(results)
        }
    }
    route("/evaluator/hand-rank") {
        post {
            val log = call.application.environment.log
            val cards = call.receive<List<Card>>()
            val evaluator = Evaluator { log.debug(it) }
            val results: Short = evaluator.evaluateCards(cards)
            call.respond(results)
        }
    }
}
