package com.ghn.poker.feature.auth.domain.usecase

import com.ghn.poker.core.network.ApiResponse

interface LoginUseCase {
    suspend fun login(username: String, password: String): ApiResponse<Unit, Exception>
}
