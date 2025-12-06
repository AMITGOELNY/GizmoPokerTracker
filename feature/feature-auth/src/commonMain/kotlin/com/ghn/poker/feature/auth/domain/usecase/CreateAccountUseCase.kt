package com.ghn.poker.feature.auth.domain.usecase

import com.ghn.poker.core.network.ApiResponse

interface CreateAccountUseCase {
    suspend fun create(username: String, password: String): ApiResponse<Unit, Exception>
}
