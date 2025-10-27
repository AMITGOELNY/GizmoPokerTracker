package com.ghn.routes

import com.ghn.model.User
import com.ghn.repository.ApiCallResult
import com.ghn.service.UserService
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Routing
import io.ktor.server.routing.post
import org.koin.ktor.ext.inject

/**
 * This function configures the authentication routes for the application.
 * It handles user login and user creation requests.
 */
internal fun Routing.auth() {
    val userService by inject<UserService>()
    post("/login") {
        val user = call.receive<User>()
        when (val result = userService.login(user.username, user.password)) {
            ApiCallResult.BadPassword,
            ApiCallResult.NotFound,
            ApiCallResult.Unauthorized ->
                call.respond(HttpStatusCode.Unauthorized, "Invalid credentials")

            is ApiCallResult.Failure ->
                call.respond(HttpStatusCode.InternalServerError, "An error occurred")

            is ApiCallResult.Success -> call.respondText(result.data)
        }
    }
    post("/register") {
        val user = call.receive<User>()
        val result = userService.create(user)
        call.respond(HttpStatusCode.OK)
    }
}
