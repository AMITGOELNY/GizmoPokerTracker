package com.ghn.poker.tracker.domain.repository

import com.ghn.poker.tracker.data.sources.remote.ApiResponse

interface LoginRepository {
    suspend fun login(username: String, password: String): ApiResponse<Unit, Exception>
}
