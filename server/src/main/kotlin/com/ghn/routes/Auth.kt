package com.ghn.routes

import com.ghn.model.RefreshTokenRequest
import com.ghn.model.User
import com.ghn.repository.ApiCallResult
import com.ghn.service.RefreshTokenService
import com.ghn.service.UserService
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.post
import org.koin.ktor.ext.inject

/**
 * This function configures the authentication routes for the application.
 * It handles user login, user creation, and token refresh requests.
 */
internal fun Routing.auth() {
    val userService by inject<UserService>()
    val refreshTokenService by inject<RefreshTokenService>()

    post("/login") {
        val user = call.receive<User>()
        when (val result = userService.login(user.username, user.password)) {
            ApiCallResult.BadPassword,
            ApiCallResult.NotFound,
            ApiCallResult.Unauthorized ->
                call.respond(HttpStatusCode.Unauthorized, "Invalid credentials")

            is ApiCallResult.Failure ->
                call.respond(HttpStatusCode.InternalServerError, "An error occurred")

            is ApiCallResult.Success -> call.respond(HttpStatusCode.OK, result.data)

            ApiCallResult.AlreadyExists ->
                call.respond(HttpStatusCode.InternalServerError, "Unexpected error")
        }
    }

    post("/refresh") {
        val request = call.receive<RefreshTokenRequest>()
        when (val result = refreshTokenService.refresh(request.refreshToken)) {
            ApiCallResult.BadPassword,
            ApiCallResult.NotFound,
            ApiCallResult.Unauthorized ->
                call.respond(HttpStatusCode.Unauthorized, "Invalid or expired refresh token")

            is ApiCallResult.Failure ->
                call.respond(HttpStatusCode.InternalServerError, "An error occurred")

            is ApiCallResult.Success -> call.respond(HttpStatusCode.OK, result.data)

            ApiCallResult.AlreadyExists ->
                call.respond(HttpStatusCode.InternalServerError, "Unexpected error")
        }
    }

    post("/register") {
        val user = call.receive<User>()
        when (val result = userService.create(user)) {
            ApiCallResult.AlreadyExists ->
                call.respond(HttpStatusCode.Conflict, "Username already exists")

            is ApiCallResult.Failure ->
                call.respond(HttpStatusCode.InternalServerError, "Registration failed")

            is ApiCallResult.Success ->
                call.respond(HttpStatusCode.Created, "User registered successfully")

            ApiCallResult.BadPassword,
            ApiCallResult.NotFound,
            ApiCallResult.Unauthorized ->
                call.respond(HttpStatusCode.InternalServerError, "Unexpected error")
        }
    }

    authenticate {
        post("/logout") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.payload?.getClaim("id")?.asInt()

            if (userId == null) {
                call.respond(HttpStatusCode.Unauthorized, "Invalid token")
                return@post
            }

            when (refreshTokenService.logout(userId)) {
                is ApiCallResult.Success ->
                    call.respond(HttpStatusCode.OK, "Logged out successfully")

                else ->
                    call.respond(HttpStatusCode.InternalServerError, "Logout failed")
            }
        }
    }
}
