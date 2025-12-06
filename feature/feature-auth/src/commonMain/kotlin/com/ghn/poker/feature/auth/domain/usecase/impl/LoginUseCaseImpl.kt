package com.ghn.poker.feature.auth.domain.usecase.impl

import com.ghn.poker.core.network.ApiResponse
import com.ghn.poker.feature.auth.domain.repository.LoginRepository
import com.ghn.poker.feature.auth.domain.usecase.LoginUseCase
import org.koin.core.annotation.Factory

@Factory([LoginUseCase::class])
class LoginUseCaseImpl(
    private val loginRepository: LoginRepository
) : LoginUseCase {
    override suspend fun login(username: String, password: String): ApiResponse<Unit, Exception> {
        return loginRepository.login(username, password)
    }
}
