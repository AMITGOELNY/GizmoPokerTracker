package com.ghn.gizmodb.evaluator.models

class Evaluator {

    suspend fun evaluateCards(cards: List<Int>): Short {
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
        return when(val size= cards.size) {
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
