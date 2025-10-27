package com.ghn.service

import com.ghn.gizmodb.common.models.Card
import com.ghn.gizmodb.common.models.EvaluatorResponse

interface EvaluatorService {
    suspend fun evaluateHands(
        heroCards: List<Card>,
        villainCards: List<Card>,
        boardCards: List<Card>,
        simulationCount: Int
    ): EvaluatorResponse

    suspend fun evaluateHandRank(cards: List<Card>): Short
}
