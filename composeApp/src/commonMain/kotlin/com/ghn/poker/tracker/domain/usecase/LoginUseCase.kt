package com.ghn.poker.tracker.domain.usecase

import com.ghn.poker.tracker.data.sources.remote.ApiResponse

interface LoginUseCase {
    suspend fun login(username: String, password: String): ApiResponse<Unit, Exception>
}
