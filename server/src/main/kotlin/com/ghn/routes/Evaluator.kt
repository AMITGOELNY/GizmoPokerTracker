package com.ghn.routes

import com.ghn.gizmodb.common.models.Card
import com.ghn.gizmodb.common.models.EvaluatorRequest
import com.ghn.gizmodb.common.models.HandRankResponse
import com.ghn.service.EvaluatorService
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

internal fun Routing.evaluator() {
    val evaluatorService by inject<EvaluatorService>()

    route("/evaluator") {
        post {
            try {
                val request = call.receive<EvaluatorRequest>()
                val results = evaluatorService.evaluateHands(
                    heroCards = request.heroCards,
                    villainCards = request.villainCards,
                    boardCards = request.boardCardsFiltered,
                    simulationCount = request.simulationCount
                )
                call.respond(HttpStatusCode.OK, results)
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            } catch (e: IllegalStateException) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to e.message))
            } catch (e: Exception) {
                call.application.environment.log.error("Unexpected error in evaluator endpoint", e)
                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("error" to "An unexpected error occurred")
                )
            }
        }
    }

    route("/evaluator/hand-rank") {
        post {
            try {
                val cards = call.receive<List<Card>>()
                val rank = evaluatorService.evaluateHandRank(cards)
                call.respond(HttpStatusCode.OK, HandRankResponse(rank))
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            } catch (e: IllegalStateException) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to e.message))
            } catch (e: Exception) {
                call.application.environment.log.error("Unexpected error in hand-rank endpoint", e)
                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("error" to "An unexpected error occurred")
                )
            }
        }
    }
}
