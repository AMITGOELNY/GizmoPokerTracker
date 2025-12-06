package com.ghn.poker.feature.tracker.domain.model

import com.ghn.gizmodb.common.models.Venue
import com.ghn.poker.core.common.util.DAY_MONTH_AND_YEAR_FORMAT
import com.ghn.poker.core.common.util.format
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.math.abs
import kotlin.math.roundToInt

data class SessionData(
    val date: kotlin.time.Instant,
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
