package com.ghn.gizmodb.evaluator.models

object Hash {

    private const val LENGTH = 13

    fun hashQuinary(q: ByteArray, k: Int): Int {
        var kLocal = k
        var sum = 0

        for (i in 0 until LENGTH) {
            sum += DPTables.DP[q[i].toInt()][LENGTH - i - 1][kLocal]
            kLocal -= q[i].toInt()
            if (kLocal <= 0) break
        }

        return sum
    }
}
