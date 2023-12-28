package com.ghn.poker.tracker.data.sources.remote

import com.ghn.gizmodb.common.models.SessionDTO

interface SessionRemoteDataSource {
    suspend fun getSessions(): ApiResponse<List<SessionDTO>, Exception>
    suspend fun createSession(session: SessionDTO): ApiResponse<Unit, Exception>
}
