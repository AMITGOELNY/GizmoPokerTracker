package com.ghn.routes

import com.ghn.model.User
import com.ghn.repository.ApiCallResult
import com.ghn.service.UserService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Routing
import io.ktor.server.routing.post
import org.koin.ktor.ext.inject

internal fun Routing.auth() {
    val userService by inject<UserService>()
    post("/login") {
        val user = call.receive<User>()
        when (val result = userService.login(user.username, user.password)) {
            ApiCallResult.BadPassword ->
                call.respond(
                    HttpStatusCode.BadRequest,
                    "User not found for username: ${user.username}"
                )

            is ApiCallResult.Failure -> call.respond(HttpStatusCode.InternalServerError)
            ApiCallResult.NotFound ->
                call.respond(HttpStatusCode.BadRequest, "user: ${user.username} not found")

            is ApiCallResult.Success -> call.respondText(result.data)
            ApiCallResult.Unauthorized -> call.respond(HttpStatusCode.Unauthorized)
        }
    }
    post("/createUser") {
        val user = call.receive<User>()
        val result = userService.create(user)
        call.respond(HttpStatusCode.OK)
    }
}
