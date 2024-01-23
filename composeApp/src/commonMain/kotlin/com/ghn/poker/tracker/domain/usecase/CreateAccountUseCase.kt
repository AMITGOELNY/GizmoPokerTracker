package com.ghn.poker.tracker.domain.usecase

import com.ghn.poker.tracker.data.sources.remote.ApiResponse

interface CreateAccountUseCase {
    suspend fun create(username: String, password: String): ApiResponse<Unit, Exception>
}
