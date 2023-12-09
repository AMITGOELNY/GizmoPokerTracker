package com.ghn.poker.tracker.data.repository

import co.touchlab.kermit.Logger
import com.ghn.poker.tracker.data.database.SessionDao
import com.ghn.poker.tracker.data.sources.remote.SessionRemoteDataSource
import com.ghn.poker.tracker.domain.repository.SessionRepository
import com.ghn.poker.tracker.util.randomUUID
import kotlinx.datetime.LocalDateTime
import org.koin.core.annotation.Single

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
            session.Session(
                id = randomUUID(),
                date = date.toString(),
                startAmount = startAmount?.toString(),
                endAmount = endAmount?.toString()
            )
        )
    }

    override suspend fun getSessions(): List<Session> {
        val sessions = remoteDataSource.getSessions()
        return sessions.map {
            Logger.d { "session date: ${it.date}" }
            Session(
                id = it.id,
                date = it.date,
                startAmount = it.startAmount,
                endAmount = it.endAmount,
            )
        }

//        return dao.getSessions()
    }
}

data class Session(
    val id: String,
    val date: String,
    val startAmount: String?,
    val endAmount: String?,
)
