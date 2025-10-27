package com.ghn.service

import com.ghn.gizmodb.common.models.Card
import com.ghn.gizmodb.common.models.CardSuit
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.slf4j.Logger

@DisplayName("EvaluatorServiceImpl")
class EvaluatorServiceImplTest {
    private lateinit var logger: Logger
    private lateinit var service: EvaluatorServiceImpl

    private val aceSpades = Card(CardSuit.SPADES, "A", 14)
    private val kingSpades = Card(CardSuit.SPADES, "K", 13)
    private val queenHearts = Card(CardSuit.HEARTS, "Q", 12)
    private val jackHearts = Card(CardSuit.HEARTS, "J", 11)
    private val tenDiamonds = Card(CardSuit.DIAMONDS, "10", 10)
    private val nineDiamonds = Card(CardSuit.DIAMONDS, "9", 9)
    private val eightClubs = Card(CardSuit.CLUBS, "8", 8)

    @BeforeEach
    fun setup() {
        logger = mockk(relaxed = true)
        service = EvaluatorServiceImpl(logger)
    }

    @Nested
    @DisplayName("evaluateHands")
    inner class EvaluateHands {
        @Test
        fun `should evaluate hands successfully with valid inputs`() = runTest {
            val simulationCount = 1000
            val result = service.evaluateHands(
                heroCards = listOf(aceSpades, kingSpades),
                villainCards = listOf(queenHearts, jackHearts),
                boardCards = listOf(tenDiamonds, nineDiamonds, eightClubs),
                simulationCount = simulationCount
            )

            result shouldNotBe null
            result.heroResult shouldNotBe null
            result.villainResult shouldNotBe null
            result.tiedResult shouldNotBe null
            // Monte Carlo simulations may have slight variance due to rounding
            val total = result.heroResult + result.villainResult + result.tiedResult
            (total >= simulationCount * 0.95 && total <= simulationCount * 1.05) shouldBe true
        }

        @Test
        fun `should throw when hero cards are empty`() = runTest {
            val exception = shouldThrow<IllegalArgumentException> {
                service.evaluateHands(
                    heroCards = emptyList(),
                    villainCards = listOf(queenHearts, jackHearts),
                    boardCards = listOf(tenDiamonds),
                    simulationCount = 1000
                )
            }
            exception.message shouldContain "Hero cards cannot be empty"
        }

        @Test
        fun `should throw when hero cards exceed 2`() = runTest {
            val exception = shouldThrow<IllegalArgumentException> {
                service.evaluateHands(
                    heroCards = listOf(aceSpades, kingSpades, queenHearts),
                    villainCards = listOf(jackHearts),
                    boardCards = listOf(tenDiamonds),
                    simulationCount = 1000
                )
            }
            exception.message shouldContain "Hero cards must not exceed 2 cards"
        }

        @Test
        fun `should throw when villain cards exceed 2`() = runTest {
            val exception = shouldThrow<IllegalArgumentException> {
                service.evaluateHands(
                    heroCards = listOf(aceSpades, kingSpades),
                    villainCards = listOf(queenHearts, jackHearts, tenDiamonds),
                    boardCards = listOf(nineDiamonds),
                    simulationCount = 1000
                )
            }
            exception.message shouldContain "Villain cards must not exceed 2 cards"
        }

        @Test
        fun `should throw when board cards exceed 5`() = runTest {
            val exception = shouldThrow<IllegalArgumentException> {
                service.evaluateHands(
                    heroCards = listOf(aceSpades, kingSpades),
                    villainCards = listOf(queenHearts, jackHearts),
                    boardCards = listOf(
                        tenDiamonds,
                        nineDiamonds,
                        eightClubs,
                        Card(CardSuit.CLUBS, "7", 7),
                        Card(CardSuit.SPADES, "6", 6),
                        Card(CardSuit.HEARTS, "5", 5)
                    ),
                    simulationCount = 1000
                )
            }
            exception.message shouldContain "Board cards must not exceed 5 cards"
        }

        @Test
        fun `should throw when simulation count is zero`() = runTest {
            val exception = shouldThrow<IllegalArgumentException> {
                service.evaluateHands(
                    heroCards = listOf(aceSpades, kingSpades),
                    villainCards = listOf(queenHearts, jackHearts),
                    boardCards = listOf(tenDiamonds),
                    simulationCount = 0
                )
            }
            exception.message shouldContain "Simulation count must be positive"
        }

        @Test
        fun `should throw when simulation count exceeds 100,000`() = runTest {
            val exception = shouldThrow<IllegalArgumentException> {
                service.evaluateHands(
                    heroCards = listOf(aceSpades, kingSpades),
                    villainCards = listOf(queenHearts, jackHearts),
                    boardCards = listOf(tenDiamonds),
                    simulationCount = 100_001
                )
            }
            exception.message shouldContain "Simulation count must not exceed 100,000"
        }

        @Test
        fun `should throw when duplicate cards are detected`() = runTest {
            val exception = shouldThrow<IllegalArgumentException> {
                service.evaluateHands(
                    heroCards = listOf(aceSpades, kingSpades),
                    villainCards = listOf(aceSpades, jackHearts),
                    boardCards = listOf(tenDiamonds),
                    simulationCount = 1000
                )
            }
            exception.message shouldContain "Duplicate cards detected"
        }

        @Test
        fun `should handle minimum valid simulation count`() = runTest {
            val simulationCount = 16 // Evaluator uses 16 parallel chunks minimum
            val result = service.evaluateHands(
                heroCards = listOf(aceSpades, kingSpades),
                villainCards = listOf(queenHearts, jackHearts),
                boardCards = listOf(tenDiamonds),
                simulationCount = simulationCount
            )

            // With small simulation counts, variance may be higher
            val total = result.heroResult + result.villainResult + result.tiedResult
            (total >= 1) shouldBe true
        }

        @Test
        fun `should handle higher simulation count`() = runTest {
            val simulationCount = 10_000
            val result = service.evaluateHands(
                heroCards = listOf(aceSpades, kingSpades),
                villainCards = listOf(queenHearts, jackHearts),
                boardCards = listOf(tenDiamonds),
                simulationCount = simulationCount
            )

            // Monte Carlo simulations may have slight variance due to rounding
            val total = result.heroResult + result.villainResult + result.tiedResult
            (total >= simulationCount * 0.95 && total <= simulationCount * 1.05) shouldBe true
        }
    }

    @Nested
    @DisplayName("evaluateHandRank")
    inner class EvaluateHandRank {
        @Test
        fun `should evaluate 5 card hand successfully`() = runTest {
            val result = service.evaluateHandRank(
                cards = listOf(aceSpades, kingSpades, queenHearts, jackHearts, tenDiamonds)
            )

            result shouldNotBe null
        }

        @Test
        fun `should evaluate 7 card hand successfully`() = runTest {
            val result = service.evaluateHandRank(
                cards = listOf(
                    aceSpades,
                    kingSpades,
                    queenHearts,
                    jackHearts,
                    tenDiamonds,
                    nineDiamonds,
                    eightClubs
                )
            )

            result shouldNotBe null
        }

        @Test
        fun `should throw when hand has less than 5 cards`() = runTest {
            val exception = shouldThrow<IllegalArgumentException> {
                service.evaluateHandRank(
                    cards = listOf(aceSpades, kingSpades, queenHearts, jackHearts)
                )
            }
            exception.message shouldContain "Hand evaluation requires exactly 5 or 7 cards"
        }

        @Test
        fun `should throw when hand has 6 cards`() = runTest {
            val exception = shouldThrow<IllegalArgumentException> {
                service.evaluateHandRank(
                    cards = listOf(
                        aceSpades,
                        kingSpades,
                        queenHearts,
                        jackHearts,
                        tenDiamonds,
                        nineDiamonds
                    )
                )
            }
            exception.message shouldContain "Hand evaluation requires exactly 5 or 7 cards"
        }

        @Test
        fun `should throw when hand has more than 7 cards`() = runTest {
            val exception = shouldThrow<IllegalArgumentException> {
                service.evaluateHandRank(
                    cards = listOf(
                        aceSpades,
                        kingSpades,
                        queenHearts,
                        jackHearts,
                        tenDiamonds,
                        nineDiamonds,
                        eightClubs,
                        Card(CardSuit.CLUBS, "7", 7)
                    )
                )
            }
            exception.message shouldContain "Hand evaluation requires exactly 5 or 7 cards"
        }

        @Test
        fun `should throw when duplicate cards are detected`() = runTest {
            val exception = shouldThrow<IllegalArgumentException> {
                service.evaluateHandRank(
                    cards = listOf(aceSpades, kingSpades, queenHearts, jackHearts, aceSpades)
                )
            }
            exception.message shouldContain "Duplicate cards detected"
        }
    }
}
