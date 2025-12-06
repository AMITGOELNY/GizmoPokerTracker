package com.ghn.poker.feature.auth.domain.usecase.impl

import com.ghn.poker.core.network.ApiResponse
import com.ghn.poker.feature.auth.domain.repository.LoginRepository
import com.ghn.poker.feature.auth.domain.usecase.CreateAccountUseCase
import org.koin.core.annotation.Factory

@Factory([CreateAccountUseCase::class])
class CreateAccountUseCaseImpl(
    private val loginRepository: LoginRepository
) : CreateAccountUseCase {
    override suspend fun create(username: String, password: String): ApiResponse<Unit, Exception> {
        return loginRepository.create(username, password)
    }
}
