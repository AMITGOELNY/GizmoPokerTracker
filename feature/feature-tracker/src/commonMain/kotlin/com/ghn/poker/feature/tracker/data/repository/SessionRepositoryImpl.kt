package com.ghn.poker.feature.tracker.data.repository

import co.touchlab.kermit.Logger
import com.ghn.gizmodb.common.models.GameType
import com.ghn.gizmodb.common.models.SessionDTO
import com.ghn.gizmodb.common.models.Venue
import com.ghn.poker.core.common.util.randomUUID
import com.ghn.poker.core.database.SessionDao
import com.ghn.poker.core.network.ApiResponse
import com.ghn.poker.feature.tracker.data.sources.remote.SessionRemoteDataSource
import com.ghn.poker.feature.tracker.domain.model.Session
import com.ghn.poker.feature.tracker.domain.repository.SessionRepository
import kotlinx.datetime.LocalDateTime
import org.koin.core.annotation.Single
import session.Session as DbSession

@Single([SessionRepository::class])
class SessionRepositoryImpl(
    private val dao: SessionDao,
    private val remoteDataSource: SessionRemoteDataSource
) : SessionRepository {
    override suspend fun insertSession(
        date: LocalDateTime,
        startAmount: Double?,
        endAmount: Double?
    ) {
        dao.insertSession(
            DbSession(
                id = randomUUID(),
                date = date.toString(),
                startAmount = startAmount?.toString(),
                endAmount = endAmount?.toString()
            )
        )
    }

    override suspend fun createSession(
        date: kotlin.time.Instant,
        startAmount: Double?,
        endAmount: Double?,
        gameType: GameType,
        venue: Venue,
    ): ApiResponse<Unit, Exception> {
        val sessionDTO = SessionDTO(
            id = randomUUID(),
            date = date,
            startamount = startAmount?.toString(),
            endamount = endAmount?.toString(),
            gametype = gameType,
            venue = venue,
            userid = -1
        )
        return remoteDataSource.createSession(sessionDTO)
    }

    override suspend fun getSessions(): ApiResponse<List<Session>, Exception> {
        return when (val sessions = remoteDataSource.getSessions()) {
            is ApiResponse.Error -> sessions
            is ApiResponse.Success -> ApiResponse.Success(
                sessions.body.map {
                    Logger.d { "session date: ${it.date}" }
                    Session(
                        id = it.id,
                        date = it.date,
                        startAmount = it.startamount,
                        endAmount = it.endamount,
                        venue = it.venue
                    )
                }
            )
        }
    }
}
