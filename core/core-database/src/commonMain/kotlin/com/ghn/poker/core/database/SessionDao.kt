package com.ghn.poker.core.database

import org.koin.core.annotation.Single
import session.Session

@Single([SessionDao::class])
class SessionDao(databaseProvider: DatabaseProvider) {
    private val dbRef = databaseProvider.database

    suspend fun insertSession(session: Session) {
        dbRef.sessionQueries.insertSession(session)
    }

    suspend fun getSessions(): List<Session> {
        return dbRef.sessionQueries.getSessions().executeAsList()
    }
}
