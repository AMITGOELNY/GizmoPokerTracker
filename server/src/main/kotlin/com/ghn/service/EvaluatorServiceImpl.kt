package com.ghn.service

import com.ghn.gizmodb.common.models.Card
import com.ghn.gizmodb.common.models.EvaluatorResponse
import com.ghn.gizmodb.evaluator.models.Evaluator
import kotlinx.coroutines.CancellationException
import org.slf4j.Logger

class EvaluatorServiceImpl(
    private val logger: Logger
) : EvaluatorService {
    private val evaluator = Evaluator { message -> logger.debug(message) }

    override suspend fun evaluateHands(
        heroCards: List<Card>,
        villainCards: List<Card>,
        boardCards: List<Card>,
        simulationCount: Int
    ): EvaluatorResponse {
        // Validate inputs
        require(heroCards.isNotEmpty()) { "Hero cards cannot be empty" }
        require(heroCards.size <= 2) { "Hero cards must not exceed 2 cards" }
        require(villainCards.size <= 2) { "Villain cards must not exceed 2 cards" }
        require(boardCards.size <= 5) { "Board cards must not exceed 5 cards" }
        require(simulationCount > 0) { "Simulation count must be positive" }
        require(simulationCount <= 100_000) { "Simulation count must not exceed 100,000" }

        // Check for duplicate cards
        val allCards = heroCards + villainCards + boardCards
        require(allCards.size == allCards.distinct().size) { "Duplicate cards detected" }

        try {
            logger.info(
                "Evaluating hands: hero=${heroCards.size} cards, villain=${villainCards.size} cards, " +
                    "board=${boardCards.size} cards, simulations=$simulationCount"
            )

            val result = evaluator.processCards(
                heroCards = heroCards,
                villain = villainCards,
                board = boardCards,
                simulationCount = simulationCount
            )

            logger.info(
                "Evaluation complete: hero=${result.heroResult}, villain=${result.villainResult}, " +
                    "tied=${result.tiedResult}"
            )

            return result
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            logger.error("Failed to evaluate hands", e)
            throw IllegalStateException("Failed to evaluate poker hands: ${e.message}", e)
        }
    }

    override suspend fun evaluateHandRank(cards: List<Card>): Short {
        // Validate inputs
        require(cards.size == 5 || cards.size == 7) {
            "Hand evaluation requires exactly 5 or 7 cards, got ${cards.size}"
        }

        // Check for duplicate cards
        require(cards.size == cards.distinct().size) { "Duplicate cards detected" }

        try {
            logger.debug("Evaluating hand rank for ${cards.size} cards")
            val rank = evaluator.evaluateCards(cards)
            logger.debug("Hand rank: $rank")
            return rank
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            logger.error("Failed to evaluate hand rank", e)
            throw IllegalStateException("Failed to evaluate hand rank: ${e.message}", e)
        }
    }
}
