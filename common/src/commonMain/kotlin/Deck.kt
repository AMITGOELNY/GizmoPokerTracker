package com.ghn.gizmodb.common.models

import kotlinx.serialization.Serializable

object Deck {
    val cards: List<Card> = buildList {
        CardSuit.entries.forEach {
            addAll(generateCardsBySuit(it))
        }
    }

    private fun generateCardsBySuit(suit: CardSuit) = listOf(
        Card(suit, "2", 2),
        Card(suit, "3", 3),
        Card(suit, "4", 4),
        Card(suit, "5", 5),
        Card(suit, "6", 6),
        Card(suit, "7", 7),
        Card(suit, "8", 8),
        Card(suit, "9", 9),
        Card(suit, "10", 10),
        Card(suit, "J", 11),
        Card(suit, "Q", 12),
        Card(suit, "K", 13),
        Card(suit, "A", 14),
    )
}

enum class CardSuit {
    HEARTS,
    DIAMONDS,
    SPADES,
    CLUBS
}

@Serializable
data class Card(
    val suit: CardSuit,
    val name: String,
    val value: Int
)
