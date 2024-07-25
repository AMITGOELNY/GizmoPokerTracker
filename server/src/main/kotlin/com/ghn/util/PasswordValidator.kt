package com.ghn.util

import org.bouncycastle.crypto.generators.OpenBSDBCrypt
import kotlin.random.Random

object PasswordValidator {

    const val USERNAME_LENGTH_MIN = 4
    const val USERNAME_LENGTH_MAX = 12
    private const val PASSWORD_LENGTH_MIN = 8
    private const val PASSWORD_LENGTH_MAX = 30

    private const val SALT_BYTES = 16
    private const val BCRYPT_COST = 10

    fun hashPassword(password: String): String {
        require(password.length in PASSWORD_LENGTH_MIN..PASSWORD_LENGTH_MAX) {
            "Expected password to be in $PASSWORD_LENGTH_MIN..$PASSWORD_LENGTH_MAX but was ${password.length}"
        }
        val salt = Random.nextBytes(SALT_BYTES)
        return OpenBSDBCrypt.generate(password.encodeToByteArray(), salt, BCRYPT_COST)
    }

    fun verifyPassword(checkPassword: String, hashString: String): Boolean {
        return OpenBSDBCrypt.checkPassword(hashString, checkPassword.toCharArray())
    }
}
