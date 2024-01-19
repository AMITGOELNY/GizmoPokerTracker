package com.ghn.poker.tracker.domain.usecase

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.ghn.gizmodb.common.models.GameType
import com.ghn.gizmodb.common.models.Venue
import com.ghn.poker.tracker.data.sources.remote.ApiResponse
import com.ghn.poker.tracker.util.DAY_MONTH_AND_YEAR_FORMAT
import com.ghn.poker.tracker.util.format
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.math.abs
import kotlin.math.roundToInt

interface SessionUseCase {
    suspend fun insertSession(
        date: LocalDateTime,
        startAmount: Double?,
        endAmount: Double?
    )

    suspend fun createSession(
        date: Instant,
        startAmount: Double?,
        endAmount: Double?,
        gameType: GameType,
        venue: Venue,
    ): ApiResponse<Unit, Exception>

    suspend fun getSessions(): ApiResponse<List<SessionData>, Exception>
}

data class SessionData(
    val date: Instant,
    val startAmount: String?,
    val endAmount: String?,
    val venue: Venue?,
) {
    val formattedDate: String
        get() {
            val localDatetime = date.toLocalDateTime(TimeZone.UTC)
            return localDatetime.format(DAY_MONTH_AND_YEAR_FORMAT).orEmpty()
        }

    val netProfit: String
        get() =
            when {
                startAmount != null || endAmount != null -> {
                    val start = startAmount?.toDoubleOrNull() ?: 0.0
                    val end = endAmount?.toDoubleOrNull() ?: 0.0
                    val profit = end - start
                    val prefix = if (profit < 0.0) "-" else ""
                    "$prefix$${abs(profit).roundToInt()}"
                }

                else -> "N/A"
            }
}

val SessionData.netAmountColor: Color
    @Composable
    get() {
        val profit = netProfit
        return when {
            profit == "N/A" -> Color(0xFFCCCCCC)
            profit.startsWith("-") -> MaterialTheme.colorScheme.error
            else -> Color.Green
        }
    }
