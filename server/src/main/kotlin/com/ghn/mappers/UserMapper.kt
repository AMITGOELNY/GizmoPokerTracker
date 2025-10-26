package com.ghn.mappers

import com.ghn.gizmodb.common.models.UserDTO
import com.ghn.model.User
import com.ghn.util.PasswordValidator
import kotlin.time.Clock

fun User.toUserDTO(): UserDTO = UserDTO(
    id = null,
    username = username,
    password = PasswordValidator.hashPassword(password),
    createdAt = Clock.System.now(),
    updatedAt = Clock.System.now(),
)
