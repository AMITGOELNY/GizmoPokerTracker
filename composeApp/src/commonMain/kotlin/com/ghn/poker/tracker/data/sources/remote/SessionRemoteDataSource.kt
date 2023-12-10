package com.ghn.poker.tracker.data.sources.remote

import com.ghn.gizmodb.common.models.SessionDTO

interface SessionRemoteDataSource {
    suspend fun getSessions(): List<SessionDTO>
}
