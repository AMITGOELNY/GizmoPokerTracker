package com.ghn.poker.tracker.domain.usecase.impl

import com.ghn.poker.tracker.data.sources.remote.ApiResponse
import com.ghn.poker.tracker.domain.repository.LoginRepository
import com.ghn.poker.tracker.domain.usecase.CreateAccountUseCase
import org.koin.core.annotation.Factory

@Factory([CreateAccountUseCase::class])
class CreateAccountUseCaseImpl(
    private val loginRepository: LoginRepository
) : CreateAccountUseCase {
    override suspend fun create(username: String, password: String): ApiResponse<Unit, Exception> {
        return loginRepository.create(username, password)
    }
}
