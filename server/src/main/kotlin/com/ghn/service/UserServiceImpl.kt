package com.ghn.service

import com.ghn.model.User
import com.ghn.repository.ApiCallResult
import com.ghn.repository.UserRepository

class UserServiceImpl(
    private val repository: UserRepository
) : UserService {
    override fun login(username: String, password: String): ApiCallResult<String> {
        return repository.login(username, password)
    }

    override fun create(user: User): ApiCallResult<Unit> {
        return repository.create(user)
    }
}
