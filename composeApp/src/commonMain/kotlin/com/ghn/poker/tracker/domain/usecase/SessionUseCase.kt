package com.ghn.poker.tracker.domain.usecase

import kotlinx.datetime.LocalDateTime

interface SessionUseCase {
    suspend fun insertSession(
        date: LocalDateTime,
        startAmount: Double?,
        endAmount: Double?
    )
}
