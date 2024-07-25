package com.ghn.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.ghn.gizmodb.common.models.UserDTO
import java.util.Date

object JwtConfig {

    private const val ISSUER = "com.ghn"
    private const val VALIDITY_IN_MS = 36_000_00 * 96
    private lateinit var algorithm: Algorithm

    fun initialize(secret: String) {
        algorithm = Algorithm.HMAC512(secret)
    }

    val verifier: JWTVerifier by lazy {
        JWT.require(algorithm)
            .withIssuer(ISSUER)
            .build()
    }

    /**
     * Produce a token for this combination of User and Account
     */
    fun makeToken(user: UserDTO): String = JWT.create()
        .withSubject("Authentication")
        .withIssuer(ISSUER)
        .withClaim("username", user.username)
        .withClaim("id", user.id)
        .withExpiresAt(getExpiration())
        .sign(algorithm)

    /**
     * Calculate the expiration Date based on current time + the given validity
     */
    private fun getExpiration() = Date(System.currentTimeMillis() + VALIDITY_IN_MS)
}
