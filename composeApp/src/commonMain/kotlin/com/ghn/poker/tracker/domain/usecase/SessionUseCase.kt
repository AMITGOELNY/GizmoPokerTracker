package com.ghn.poker.tracker.domain.usecase

import com.ghn.poker.tracker.util.DAY_MONTH_AND_YEAR_FORMAT
import com.ghn.poker.tracker.util.format
import kotlinx.datetime.LocalDateTime

interface SessionUseCase {
    suspend fun insertSession(
        date: LocalDateTime,
        startAmount: Double?,
        endAmount: Double?
    )

    suspend fun getSessions(): List<SessionData>
}

data class SessionData(
    val date: LocalDateTime,
    val startAmount: String?,
    val endAmount: String?,
) {
    val formattedDate: String
        get() = date.format(DAY_MONTH_AND_YEAR_FORMAT).orEmpty()
}
