package org.example.medimitr.user

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.log
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import org.example.medimitr.user.mode.LoginCredentialsRequest
import org.example.medimitr.user.mode.PasswordChange
import org.example.medimitr.user.mode.UserRequest
import org.example.medimitr.user.services.UserService
import org.koin.ktor.ext.inject
import java.util.Date

private const val EXPIRY = 6000000 // 100 minutes

fun Application.configureUserRouting() {
    val userService by inject<UserService>()

    routing {
        post("/create/user") {
            call.application.log.info("Handling POST /users request")
            try {
                val userRequest = call.receive<UserRequest>()
                call.application.log.info("Parsed user request: $userRequest")
                val userResponse = userService.createUser(userRequest)
                call.application.log.info("User created: $userResponse")
                call.respond(HttpStatusCode.Created, userResponse)
            } catch (e: Exception) {
                call.application.log.error("Failed to create user: ${e.message}")
                call.respond(HttpStatusCode.BadRequest, "Error creating user: ${e.message}")
            }
        }

        // Define routing block
        post("/login") {
            // POST /login endpoint
            val credentials = call.receive<LoginCredentialsRequest>() // Receive login credentials from request body
            val user = userService.findUserByEmail(credentials) // Find user by email
            if (user != null) { // Check credentials
                val token =
                    JWT
                        .create() // Create a new JWT
                        .withClaim("userId", user.id) // Add userId claim
                        .withExpiresAt(Date(System.currentTimeMillis() + EXPIRY)) // Set expiration (10 minutes)
                        .sign(Algorithm.HMAC256("your_secret_key")) // Sign with secret key
                call.respond(mapOf("token" to token)) // Respond with token
            } else {
                call.respond(HttpStatusCode.Unauthorized, "Invalid credentials") // Respond with error if invalid
            }
        }

        authenticate {
            get("/user") {
                // GET /orders endpoint
                val principal = call.principal<JWTPrincipal>() // Get JWT principal
                val userId =
                    principal?.payload?.getClaim("userId")?.asInt() // Extract userId
                        ?: return@get call.respond(HttpStatusCode.Unauthorized) // Return unauthorized if missing
                val user = userService.getUserById(userId) // Get user by ID
                if (user == null) {
                    call.respond(HttpStatusCode.NotFound, "User not found")
                } else {
                    call.respond(user) // Respond with user details
                }
            }

            post("/user/email") {
                // POST /user/email endpoint
                val principal = call.principal<JWTPrincipal>() // Get JWT principal
                val userId =
                    principal?.payload?.getClaim("userId")?.asInt() // Extract userId
                        ?: return@post call.respond(HttpStatusCode.Unauthorized) // Return unauthorized if missing
                val email = call.receive<String>() // Receive email from request body
                val user = userService.updateUserEmail(userId, email) // Update user email
                if (user == null) {
                    call.respond(HttpStatusCode.NotFound, "User not found")
                } else {
                    call.respond(true) // Respond with updated user details
                }
            }

            post("/user/address") {
                // POST /user/address endpoint
                val principal = call.principal<JWTPrincipal>() // Get JWT principal
                val userId =
                    principal?.payload?.getClaim("userId")?.asInt() // Extract userId
                        ?: return@post call.respond(HttpStatusCode.Unauthorized) // Return unauthorized if missing
                val address = call.receive<String>() // Receive address from request body
                val user = userService.updateUserAddress(userId, address) // Update user email
                if (user == null) {
                    call.respond(HttpStatusCode.NotFound, "User not found")
                } else {
                    call.respond(true) // Respond with updated user details
                }
            }

            post("/user/phone") {
                // POST /user/phone endpoint
                val principal = call.principal<JWTPrincipal>() // Get JWT principal
                val userId =
                    principal?.payload?.getClaim("userId")?.asInt() // Extract userId
                        ?: return@post call.respond(HttpStatusCode.Unauthorized) // Return unauthorized if missing
                val phone = call.receive<String>() // Receive address from request body
                val user = userService.updateUserPhone(userId, phone) // Update user email
                if (user == null) {
                    call.respond(HttpStatusCode.NotFound, "User not found")
                } else {
                    call.respond(true) // Respond with updated user details
                }
            }

            post("/user/password") {
                // POST /user/password endpoint
                val principal = call.principal<JWTPrincipal>() // Get JWT principal
                val userId =
                    principal?.payload?.getClaim("userId")?.asInt() // Extract userId
                        ?: return@post call.respond(HttpStatusCode.Unauthorized) // Return unauthorized if missing
                val passwordRequest = call.receive<PasswordChange>() // Receive address from request body
                val user =
                    userService.updateUserPassword(
                        userId,
                        passwordRequest.oldPassword,
                        passwordRequest.newPassword,
                    ) // Update user email
                if (user == null) {
                    call.respond(HttpStatusCode.NotFound, "User not found")
                } else {
                    call.respond(true) // Respond with updated user details
                }
            }
        }
    }
}
