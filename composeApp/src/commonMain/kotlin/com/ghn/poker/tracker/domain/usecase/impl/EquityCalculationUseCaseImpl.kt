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
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Factory
import kotlin.random.Random
import kotlin.random.nextInt
import kotlin.time.DurationUnit
import kotlin.time.measureTime

@Factory([EquityCalculationUseCase::class])
internal class EquityCalculationUseCaseImpl : EquityCalculationUseCase {

    private val results = intArrayOf(0, 0, 0)
    private val measureTimes = mutableListOf<Long>()

    override suspend fun evaluateCards(
        heroCards: List<Card?>,
        villainCards: List<Card?>,
        boardCards: List<Card?>,
        deck: List<CardState>,
    ): IntArray {
        results[0] = 0
        results[1] = 0
        results[2] = 0

        val boardCardsFiltered = boardCards.mapNotNull { it }
        val boardCardsAmountToAdd = 5 - boardCardsFiltered.size

        val time = measureTime {
            withContext(Dispatchers.IO) {
                val newDeck = deck.filter { !it.disabled }.shuffled()

                val chunkSimulate = (SIMULATION_COUNT / 8).toInt()
                val deferredEvaluators = buildList {
                    repeat((0 until 8).count()) {
                        val evaluatedChunk = async {
                            evaluateChunk(
                                newDeck = newDeck,
                                boardCardsAmountToAdd = boardCardsAmountToAdd,
                                heroCards = heroCards,
                                boardCardsFiltered = boardCardsFiltered,
                                villainCards = villainCards,
                                chunk = chunkSimulate,
                            )
                        }
                        add(evaluatedChunk)
                    }
                }

                val items = deferredEvaluators.map { it.await() }
                items.forEach {
                    results[0] += it[0]
                    results[1] += it[1]
                    results[2] += it[2]
                }
            }
        }

        Logger.d { "Evolution time $time" }
        measureTimes.add(time.toLong(DurationUnit.MILLISECONDS))

        if (measureTimes.size > 1) {
            val average =
                measureTimes.takeLast(measureTimes.size - 1).sum() / (measureTimes.size - 1)
            Logger.d { "average $average" }
        }

        return results
    }

    private suspend fun evaluateChunk(
        newDeck: List<CardState>,
        boardCardsAmountToAdd: Int,
        heroCards: List<Card?>,
        boardCardsFiltered: List<Card>,
        villainCards: List<Card?>,
        chunk: Int,
    ): IntArray {
        val results = intArrayOf(0, 0, 0)
        repeat((0 until chunk.toInt()).count()) { seq ->
            val randomBoardCards = generateSequence { Random.nextInt(newDeck.indices) }
                .distinct()
                .take(boardCardsAmountToAdd)
                .map { newDeck[it].card }
                .toPersistentList()

            val playerCardsPlusBoard =
                heroCards.mapNotNull { it } + boardCardsFiltered + randomBoardCards
            val villainCardsPlusBoard =
                villainCards.mapNotNull { it } + boardCardsFiltered + randomBoardCards

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
        // Logger.d { "Converting ${it.name}${it.suit} to binary: ${convert.toString(2)}, decimal: $convert" }
        convert
    }
}
