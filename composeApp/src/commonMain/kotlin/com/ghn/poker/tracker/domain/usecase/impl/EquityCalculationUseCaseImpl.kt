package com.ghn.poker.tracker.domain.usecase.impl

import co.touchlab.kermit.Logger
import com.ghn.gizmodb.evaluator.models.Evaluator
import com.ghn.poker.tracker.domain.model.Card
import com.ghn.poker.tracker.domain.model.CardSuit
import com.ghn.poker.tracker.domain.usecase.EquityCalculationUseCase
import com.ghn.poker.tracker.presentation.cards.CardState
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Factory
import kotlin.random.Random
import kotlin.random.nextInt

@Factory([EquityCalculationUseCase::class])
internal class EquityCalculationUseCaseImpl : EquityCalculationUseCase {

    private val evaluator = Evaluator()
    private val results = intArrayOf(0, 0, 0)

    override suspend fun evaluateCards(
        heroCards: List<Card?>,
        villainCards: List<Card?>,
        deck: List<CardState>,
    ): IntArray {
        results[0] = 0
        results[1] = 0
        results[2] = 0

        var lowestRank = Short.MAX_VALUE
        var lowestRankIndex = -1

        withContext(Dispatchers.IO) {
            val newDeck = deck.filter { !it.disabled }.shuffled()

            repeat((0 until SIMULATION_COUNT.toInt()).count()) { seq ->
                val randomBoardCards = generateSequence { Random.nextInt(newDeck.indices) }
                    .distinct()
                    .take(5)
                    .map { newDeck[it].card }
                    .toPersistentList()

                val playerCardsPlusBoard = heroCards.mapNotNull { it } + randomBoardCards
                val villainCardsPlusBoard = villainCards.mapNotNull { it } + randomBoardCards

                val cardIds = playerCardsPlusBoard.convertCardsToIds()
                val villainCardIds = villainCardsPlusBoard.convertCardsToIds()

                try {
                    val evaluator = Evaluator()
                    val heroRank = evaluator.evaluateCards(cardIds)
                    val villainRank = evaluator.evaluateCards(villainCardIds)

                    when {
                        heroRank == villainRank -> results[2]++
                        heroRank < villainRank -> results[0]++
                        else -> results[1]++
                    }
                } catch (e: Exception) {
                    Logger.e { "Failed at index: $seq" }
                    Logger.e { "player: $playerCardsPlusBoard" }
                    Logger.e { "villain: $villainCardsPlusBoard" }
                    Logger.e("", e)
//                    throw e
                }
            }
        }

        return results
    }

    companion object {
        const val SIMULATION_COUNT = 50000.0
    }
}

fun List<Card>.convertCardsToIds(): List<Int> {
    return map { card ->
        val suitValue = when (card.suit) {
            CardSuit.CLUBS -> 0
            CardSuit.DIAMONDS -> 1
            CardSuit.HEARTS -> 2
            CardSuit.SPADES -> 3
        }

        val convert = ((card.value - 2) * 4) + suitValue
//                Logger.d { "Converting ${it.name}${it.suit} to binary: ${convert.toString(2)}, decimal: $convert" }
        convert
    }
}
