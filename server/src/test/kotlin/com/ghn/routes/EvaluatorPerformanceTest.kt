package com.ghn.routes

import com.ghn.di.appModule
import com.ghn.gizmodb.common.models.Card
import com.ghn.gizmodb.common.models.CardSuit
import com.ghn.gizmodb.common.models.EvaluatorRequest
import com.ghn.gizmodb.common.models.EvaluatorResponse
import com.ghn.plugins.JwtConfig
import com.ghn.plugins.configureRouting
import com.ghn.plugins.configureSecurity
import com.ghn.plugins.configureSerialization
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.longs.shouldBeLessThan
import io.kotest.matchers.shouldBe
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.install
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.ktor.plugin.Koin
import kotlin.system.measureTimeMillis
import io.kotest.matchers.ints.shouldBeGreaterThan as shouldBeGreaterThanInt

@DisplayName("Evaluator Performance Tests")
class EvaluatorPerformanceTest {

    private val aceOfHearts = Card(suit = CardSuit.HEARTS, name = "A", value = 14)
    private val aceOfDiamonds = Card(suit = CardSuit.DIAMONDS, name = "A", value = 14)
    private val kingOfDiamonds = Card(suit = CardSuit.DIAMONDS, name = "K", value = 13)
    private val kingOfClubs = Card(suit = CardSuit.CLUBS, name = "K", value = 13)

    @Test
    @DisplayName("should complete 10k simulations in under 200ms")
    fun `test 10k simulations performance`() = testApplication {
        setupTestApplication()

        val client = createJsonClient()

        val request = EvaluatorRequest(
            heroCards = listOf(aceOfHearts, aceOfDiamonds),
            boardCardsFiltered = emptyList(),
            villainCards = listOf(kingOfDiamonds, kingOfClubs),
            simulationCount = 10_000
        )

        val timeMillis = measureTimeMillis {
            val response = client.post("/evaluator") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }

            response.status shouldBe HttpStatusCode.OK
            val result = response.body<EvaluatorResponse>()

            // Verify results are reasonable
            result.heroResult shouldBeGreaterThanInt 0
            result.villainResult shouldBeGreaterThanInt 0
        }

        println("10k simulations completed in ${timeMillis}ms")
        timeMillis shouldBeLessThan 200L
    }

    @Test
    @DisplayName("should complete 50k simulations in under 1 second")
    fun `test 50k simulations performance`() = testApplication {
        setupTestApplication()

        val client = createJsonClient()

        val request = EvaluatorRequest(
            heroCards = listOf(aceOfHearts, aceOfDiamonds),
            boardCardsFiltered = emptyList(),
            villainCards = listOf(kingOfDiamonds, kingOfClubs),
            simulationCount = 50_000
        )

        val timeMillis = measureTimeMillis {
            val response = client.post("/evaluator") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }

            response.status shouldBe HttpStatusCode.OK
            val result = response.body<EvaluatorResponse>()

            // Verify AA vs KK equity is correct (~80% for AA)
            val totalSimulations = result.heroResult + result.villainResult + result.tiedResult
            val heroWinRate = result.heroResult.toDouble() / totalSimulations

            // AA should win approximately 80-85% of the time vs KK
            heroWinRate shouldBeGreaterThan 0.78
        }

        println("50k simulations completed in ${timeMillis}ms")
        timeMillis shouldBeLessThan 1000L
    }

    @Test
    @DisplayName("should complete 100k simulations in under 2 seconds")
    fun `test 100k simulations performance`() = testApplication {
        setupTestApplication()

        val client = createJsonClient()

        val request = EvaluatorRequest(
            heroCards = listOf(aceOfHearts, aceOfDiamonds),
            boardCardsFiltered = emptyList(),
            villainCards = listOf(kingOfDiamonds, kingOfClubs),
            simulationCount = 100_000
        )

        val timeMillis = measureTimeMillis {
            val response = client.post("/evaluator") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }

            response.status shouldBe HttpStatusCode.OK
            val result = response.body<EvaluatorResponse>()

            // Verify results are complete
            val totalSimulations = result.heroResult + result.villainResult + result.tiedResult
            totalSimulations shouldBeGreaterThanInt 95_000 // Allow 5% variance
        }

        println("100k simulations completed in ${timeMillis}ms")
        timeMillis shouldBeLessThan 2000L
    }

    @Test
    @DisplayName("should handle multiple sequential requests efficiently")
    fun `test sequential requests performance`() = testApplication {
        setupTestApplication()

        val client = createJsonClient()

        val request = EvaluatorRequest(
            heroCards = listOf(aceOfHearts, aceOfDiamonds),
            boardCardsFiltered = emptyList(),
            villainCards = listOf(kingOfDiamonds, kingOfClubs),
            simulationCount = 5_000
        )

        val totalTimeMillis = measureTimeMillis {
            repeat(5) {
                val response = client.post("/evaluator") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
                response.status shouldBe HttpStatusCode.OK
            }
        }

        println("5 sequential requests (5k sims each) completed in ${totalTimeMillis}ms")
        // Should complete 5 requests in under 500ms total
        totalTimeMillis shouldBeLessThan 500L
    }

    @Test
    @DisplayName("should handle board cards efficiently")
    fun `test performance with board cards`() = testApplication {
        setupTestApplication()

        val client = createJsonClient()

        val request = EvaluatorRequest(
            heroCards = listOf(aceOfHearts, aceOfDiamonds),
            boardCardsFiltered = listOf(
                Card(suit = CardSuit.SPADES, name = "K", value = 13),
                Card(suit = CardSuit.HEARTS, name = "Q", value = 12),
                Card(suit = CardSuit.CLUBS, name = "J", value = 11)
            ),
            villainCards = listOf(kingOfDiamonds, kingOfClubs),
            simulationCount = 50_000
        )

        val timeMillis = measureTimeMillis {
            val response = client.post("/evaluator") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }

            response.status shouldBe HttpStatusCode.OK
            val result = response.body<EvaluatorResponse>()

            // With 3 board cards, fewer cards need to be dealt
            result.heroResult shouldBeGreaterThanInt 0
        }

        println("50k simulations with 3 board cards completed in ${timeMillis}ms")
        // Should be slightly faster with board cards (fewer random cards needed)
        timeMillis shouldBeLessThan 1000L
    }

    private fun ApplicationTestBuilder.setupTestApplication() {
        application {
            JwtConfig.initialize("test-secret-key")
            install(Koin) {
                modules(appModule)
            }
            configureSerialization()
            configureSecurity()
            configureRouting()
        }
    }

    private fun ApplicationTestBuilder.createJsonClient() = createClient {
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                }
            )
        }
    }
}
