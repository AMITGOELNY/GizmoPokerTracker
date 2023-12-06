package com.ghn.poker.tracker.data.database

import session.Session

class SessionDao(databaseProvider: DatabaseProvider) {
    private val dbRef = databaseProvider.database

    suspend fun insertSession(session: Session) {
        dbRef.sessionQueries.insertSession(session)
    }

    suspend fun getParticipants(): List<Session> {
        return dbRef.sessionQueries.getSessions().executeAsList()
    }
}
