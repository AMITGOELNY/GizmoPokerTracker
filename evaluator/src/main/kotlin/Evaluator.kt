package com.ghn.gizmodb.evaluator.models

import com.ghn.gizmodb.common.models.Card
import com.ghn.gizmodb.common.models.CardSuit
import com.ghn.gizmodb.common.models.Deck
import com.ghn.gizmodb.common.models.EvaluatorResponse
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import kotlin.random.Random
import kotlin.time.DurationUnit
import kotlin.time.measureTime

class Evaluator(private val logger: (String) -> Unit) {
    private val results = intArrayOf(0, 0, 0)
    private val measureTimes = mutableListOf<Long>()

    suspend fun processCards(heroCards: List<Card>, villain: List<Card>, board: List<Card>): EvaluatorResponse {
        results[0] = 0
        results[1] = 0
        results[2] = 0

        val time = measureTime {
            withContext(Dispatchers.IO) {
                val newDeck = Deck.cards.filter { !heroCards.contains(it) && !board.contains(it) && !villain.contains(it)}

                val chunkSimulate = (SIMULATION_COUNT / 16).toInt()
                val deferredEvaluators: Deferred<List<IntArray>> = async {
                    buildList {
                        repeat((0 until 16).count()) {
                            val evaluatedChunk = async(context = Dispatchers.IO) {
                                evaluateChunk(
                                    deck = newDeck,
                                    heroCards = heroCards,
                                    boardCardsFiltered = board,
                                    villainCards = villain,
                                    chunk = chunkSimulate,
                                )
                            }
                            add(evaluatedChunk)
                        }
                    }.awaitAll()
                }

                val items = deferredEvaluators.await()
                items.forEach {
                    results[0] += it[0]
                    results[1] += it[1]
                    results[2] += it[2]
                }
            }
        }

        logger("Evolution time $time")
        measureTimes.add(time.toLong(DurationUnit.MILLISECONDS))

        if (measureTimes.size > 1) {
            val average =
                measureTimes.takeLast(measureTimes.size - 1).sum() / (measureTimes.size - 1)
            logger("average $average")
        }
        return EvaluatorResponse(
            heroResult = results[0],
            villainResult = results[1],
            tiedResult = results[2]
        )
    }

    private suspend fun evaluateChunk(
        deck: List<Card>,
        heroCards: List<Card>,
        boardCardsFiltered: List<Card>,
        villainCards: List<Card>,
        chunk: Int
    ): IntArray {
        val results = intArrayOf(0, 0, 0)

        repeat((0 until chunk).count()) { seq ->

            val randomBoardCards = generateSequence { Random.nextInt(deck.size) }
                .distinct()
                .take(5 - boardCardsFiltered.size)
                .map { deck[it] }
                .toList()

            val playerCardsPlusBoard = heroCards + boardCardsFiltered + randomBoardCards
            val villainCardsPlusBoard = villainCards + boardCardsFiltered + randomBoardCards

            try {
                val heroRank = evaluateCards(playerCardsPlusBoard)
                val villainRank = evaluateCards(villainCardsPlusBoard)

                when {
                    heroRank == villainRank -> results[2]++
                    heroRank < villainRank -> results[0]++
                    else -> results[1]++
                }
            } catch (e: Exception) {
                logger("Failed at index: $seq")
                logger("player: $playerCardsPlusBoard")
                logger("villain: $villainCardsPlusBoard")
//                logger("", e)
            }
        }
        return results
    }

    suspend fun evaluateCards(originalCards: List<Card>): Short {
        val cards = originalCards.convertCardsToIds(logger)
        val suitHash = cards.sumOf { SUITBIT_BY_ID[it].toInt() }
        val flushSuit = DPTables.SUITS[suitHash] - 1

        if (flushSuit != -1) {
            var handBinary = 0

            cards.forEach {
                if (it % 4 == flushSuit) {
                    handBinary = handBinary or BINARIES_BY_ID[it].toInt()
                }
            }
            return FLUSH[handBinary]
        }

        val quinary = ByteArray(13)
        cards.forEach {
            quinary[it shr 2]++
        }
        val hash = Hash.hashQuinary(quinary, cards.size)
        return when (val size = cards.size) {
            5 -> HashTable.getNoFlush5()[hash]
            7 -> HashTable.getNoFlush7()[hash]
            else -> throw IllegalStateException("Count $size not supported")
        }
    }

    companion object {
        val SUITBIT_BY_ID = shortArrayOf(
            0x1, 0x8, 0x40, 0x200,
            0x1, 0x8, 0x40, 0x200,
            0x1, 0x8, 0x40, 0x200,
            0x1, 0x8, 0x40, 0x200,
            0x1, 0x8, 0x40, 0x200,
            0x1, 0x8, 0x40, 0x200,
            0x1, 0x8, 0x40, 0x200,
            0x1, 0x8, 0x40, 0x200,
            0x1, 0x8, 0x40, 0x200,
            0x1, 0x8, 0x40, 0x200,
            0x1, 0x8, 0x40, 0x200,
            0x1, 0x8, 0x40, 0x200,
            0x1, 0x8, 0x40, 0x200,
        )

        val BINARIES_BY_ID = shortArrayOf(
            0x1, 0x1, 0x1, 0x1,
            0x2, 0x2, 0x2, 0x2,
            0x4, 0x4, 0x4, 0x4,
            0x8, 0x8, 0x8, 0x8,
            0x10, 0x10, 0x10, 0x10,
            0x20, 0x20, 0x20, 0x20,
            0x40, 0x40, 0x40, 0x40,
            0x80, 0x80, 0x80, 0x80,
            0x100, 0x100, 0x100, 0x100,
            0x200, 0x200, 0x200, 0x200,
            0x400, 0x400, 0x400, 0x400,
            0x800, 0x800, 0x800, 0x800,
            0x1000, 0x1000, 0x1000, 0x1000,
        )

        const val SIMULATION_COUNT = 20000.0
    }
}

val Short.getHandRankName: String
    get() = when {
        this > 6185 -> "HIGH CARD"
        this > 3325 -> "ONE PAIR"
        this > 2467 -> "TWO PAIR"
        this > 1609 -> "THREE OF A KIND"
        this > 1599 -> "STRAIGHT"
        this > 322 -> "FLUSH"
        this > 166 -> "FULL HOUSE"
        this > 10 -> "FOUR OF A KIND"
        else -> "STRAIGHT FLUSH"
    }

fun List<Card>.convertCardsToIds(log: (String) -> Unit): List<Int> {
    return map { card ->
        val suitValue = when (card.suit) {
            CardSuit.CLUBS -> 0
            CardSuit.DIAMONDS -> 1
            CardSuit.HEARTS -> 2
            CardSuit.SPADES -> 3
        }

        val convert = ((card.value - 2) * 4) + suitValue
        log("Converting ${card.name}${card.suit} to binary: ${convert.toString(2)}, decimal: $convert")
        convert
    }
}
