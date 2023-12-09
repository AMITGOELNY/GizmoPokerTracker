package com.ghn.poker.tracker.data.sources.remote

import kotlinx.serialization.Serializable

interface SessionRemoteDataSource {
    suspend fun getSessions(): List<SessionDTO>
}

@Serializable
data class SessionDTO(
    val id: String,
    val date: String,
    val startAmount: String?,
    val endAmount: String?,
)
