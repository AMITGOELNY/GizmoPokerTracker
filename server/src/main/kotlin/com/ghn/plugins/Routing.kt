package com.ghn.plugins

import com.ghn.gizmodb.common.models.UserDTO
import com.ghn.gizmodb.tables.references.USER
import com.ghn.routing.evaluatorRouting
import com.ghn.routing.feedRouting
import com.ghn.routing.sessionRouting
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.autohead.AutoHeadResponse
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import kotlinx.datetime.Clock
import org.bouncycastle.crypto.generators.OpenBSDBCrypt
import org.jooq.DSLContext
import kotlin.random.Random

fun Application.configureRouting(db: DSLContext) {
    install(AutoHeadResponse)
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        post("/login") {
            val user = call.receive<User>()
            val userDTO =
                db.fetchOne(USER, USER.USERNAME.eq(user.username))?.into(UserDTO::class.java)
            if (userDTO != null) {
                val passwordValid = verifyPassword(user.password, userDTO.password)
                if (passwordValid) {
                    val token = JwtConfig.makeToken(userDTO)
                    call.respondText(token)
                } else {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        "User not found for username: ${user.username}"
                    )
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "user: ${user.username} not found")
            }
        }
        post("/createUser") {
            val user = call.receive<User>()
            val newRecord = db.newRecord(USER)
            newRecord.from(user.toUserDTO())
            newRecord.store()
            call.respond(HttpStatusCode.OK)
        }
        sessionRouting(db)
        feedRouting(db)
        evaluatorRouting()
    }
}

fun User.toUserDTO(): UserDTO = UserDTO(
    id = null,
    username = username,
    password = hashPassword(password),
    createdAt = Clock.System.now(),
    updatedAt = Clock.System.now(),
)

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

const val USERNAME_LENGTH_MIN = 4
const val USERNAME_LENGTH_MAX = 12
const val PASSWORD_LENGTH_MIN = 8
const val PASSWORD_LENGTH_MAX = 30
private const val SALT_BYTES = 16
private const val BCRYPT_COST = 10
