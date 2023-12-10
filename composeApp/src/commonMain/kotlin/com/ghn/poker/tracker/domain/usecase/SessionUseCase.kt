package com.ghn.poker.tracker.domain.usecase

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.ghn.poker.tracker.util.DAY_MONTH_AND_YEAR_FORMAT
import com.ghn.poker.tracker.util.format
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

interface SessionUseCase {
    suspend fun insertSession(
        date: LocalDateTime,
        startAmount: Double?,
        endAmount: Double?
    )

    suspend fun getSessions(): List<SessionData>
}

data class SessionData(
    val date: Instant,
    val startAmount: String?,
    val endAmount: String?,
) {
    val formattedDate: String
        get() {
            val localDatetime = date.toLocalDateTime(TimeZone.currentSystemDefault())
            return localDatetime.format(DAY_MONTH_AND_YEAR_FORMAT).orEmpty()
        }

    val netProfit: String
        get() =
            when {
                startAmount != null || endAmount != null -> {
                    val profit =
                        (endAmount?.toDoubleOrNull() ?: 0.0) - (
                            startAmount?.toDoubleOrNull()
                                ?: 0.0
                        )
                    val prefix = if (profit < 0.0) "-" else ""
                    "$prefix$" +
                        (
                            (endAmount?.toDoubleOrNull() ?: 0.0) - (
                                startAmount?.toDoubleOrNull()
                                    ?: 0.0
                            )
                        ).toString()
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
