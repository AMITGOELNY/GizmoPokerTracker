package com.ghn.gizmodb.common.models

import kotlinx.serialization.Serializable

@Serializable
data class EvaluatorRequest(
    val heroCards: List<Card>,
    val boardCardsFiltered: List<Card>,
    val villainCards: List<Card>,
)
