package com.ghn.plugins

import com.ghn.gizmodb.common.models.FeedDTO
import com.ghn.gizmodb.common.models.SessionDTO
import com.ghn.gizmodb.common.models.UserDTO
import com.ghn.gizmodb.tables.references.FEED
import com.ghn.gizmodb.tables.references.SESSION
import com.ghn.gizmodb.tables.references.USER
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
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
    }
}

private fun Routing.feedRouting(db: DSLContext) {
    route("/feed") {
        get {
            val feed = db.fetch(FEED).into(FeedDTO::class.java)
            call.respond(feed)
        }
    }
}

fun Route.sessionRouting(db: DSLContext) {
    authenticate {
        route("/sessions") {
            get {
                val principal = call.principal<JWTPrincipal>()
                val id = principal!!.payload.getClaim("id").asInt()
                val sessions = db.fetch(SESSION, SESSION.USERID.eq(id)).into(SessionDTO::class.java)
                call.respond(sessions)
            }

            post {
                val principal = call.principal<JWTPrincipal>()
                val id = principal!!.payload.getClaim("id").asInt()
                val userSession = call.receive<SessionDTO>()

                val newRecord = db.newRecord(SESSION)
                newRecord.from(userSession)
                newRecord.userid = id
                newRecord.store()

                call.respond(HttpStatusCode.OK)
            }

            route("/{id}") {
                get {
                    val principal = call.principal<JWTPrincipal>()
                    val userId = principal!!.payload.getClaim("id").asInt()
                    val id = call.parameters["id"].toString()
                    val session = db.fetchOne(SESSION, SESSION.ID.eq(id), SESSION.USERID.eq(userId))
                        ?.into(SessionDTO::class.java)
                    if (session != null) {
                        call.respond(session)
                    } else {
                        call.respond(HttpStatusCode.BadRequest, "Session not found for id: $id")
                    }
                }

                delete {
                    val principal = call.principal<JWTPrincipal>()
                    val userId = principal!!.payload.getClaim("id").asInt()
                    val id = call.parameters["id"].toString()
                    try {
                        val success = db.deleteFrom(SESSION)
                            .where(SESSION.ID.eq(id), SESSION.USERID.eq(userId))
                            .execute()
                        if (success != 0) {
                            call.respond(HttpStatusCode.OK)
                        } else {
                            call.respond(HttpStatusCode.NotAcceptable)
                        }
                    } catch (e: Exception) {
                        call.respond(
                            HttpStatusCode.InternalServerError,
                            "Delete session failed for id: $id"
                        )
                    }
                }
            }
        }
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
