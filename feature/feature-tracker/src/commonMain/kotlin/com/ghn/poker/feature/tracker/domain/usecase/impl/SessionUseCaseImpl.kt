package com.ghn.poker.feature.tracker.domain.usecase.impl

import com.ghn.gizmodb.common.models.GameType
import com.ghn.gizmodb.common.models.Venue
import com.ghn.poker.core.network.ApiResponse
import com.ghn.poker.feature.tracker.domain.model.Session
import com.ghn.poker.feature.tracker.domain.model.SessionData
import com.ghn.poker.feature.tracker.domain.repository.SessionRepository
import com.ghn.poker.feature.tracker.domain.usecase.SessionUseCase
import kotlinx.datetime.LocalDateTime
import org.koin.core.annotation.Factory

@Factory([SessionUseCase::class])
class SessionUseCaseImpl(
    private val sessionRepository: SessionRepository
) : SessionUseCase {
    override suspend fun insertSession(
        date: LocalDateTime,
        startAmount: Double?,
        endAmount: Double?
    ) {
        sessionRepository.insertSession(date, startAmount, endAmount)
    }

    override suspend fun createSession(
        date: kotlin.time.Instant,
        startAmount: Double?,
        endAmount: Double?,
        gameType: GameType,
        venue: Venue,
    ): ApiResponse<Unit, Exception> {
        return sessionRepository.createSession(date, startAmount, endAmount, gameType, venue)
    }

    override suspend fun getSessions(): ApiResponse<List<SessionData>, Exception> {
        return when (val sessions = sessionRepository.getSessions()) {
            is ApiResponse.Error -> sessions
            is ApiResponse.Success -> ApiResponse.Success(
                sessions.body.toSessionData()
            )
        }
    }
}

private fun List<Session>.toSessionData(): List<SessionData> =
    map {
        SessionData(
            date = it.date,
            startAmount = it.startAmount,
            endAmount = it.endAmount,
            venue = it.venue
        )
    }
