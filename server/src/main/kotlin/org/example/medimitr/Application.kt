package org.example.medimitr

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.application.log
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import kotlinx.serialization.json.Json
import org.example.medimitr.models.Medicines
import org.example.medimitr.models.OrderItems
import org.example.medimitr.models.Orders
import org.example.medimitr.models.Users
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.ktor.plugin.Koin // Import Koin plugin
import java.sql.Connection

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0") {
        module()
    }.start(wait = true)
}

fun Application.module() {
    val database =
        try {
            Database
                .connect(
                    url = "jdbc:mysql://localhost:3306/MedicineDB", // MySQL URL
                    driver = "com.mysql.cj.jdbc.Driver", // MySQL driver
                    user = "root", // Replace with your SQL Server username
                    password = "password", // Replace with your SQL Server password
                ).also { log.info("Database connected successfully") }
        } catch (e: Exception) {
            log.error("Failed to connect to database: ${e.message}")
            throw e // Re-throw to halt startup if connection fails
        }

    // Set transaction isolation level for better concurrency control
    TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE

    transaction {
        try {
            SchemaUtils.create(Users, Medicines, Orders, OrderItems)
        } catch (e: Exception) {
            println("Failed to create tables: ${e.message}")
        }
    }

    // Install content negotiation plugin with JSON support
    install(ContentNegotiation) {
        json(
            Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            },
        ) // Enable JSON serialization/deserialization
    }

    install(Koin) {
        printLogger()
        modules(serverModule(database)) // Load the dependency injection module
    }

    // Install JWT authentication
    install(Authentication) {
        jwt {
            verifier(JWT.require(Algorithm.HMAC256("your_secret_key")).build()) // Verify JWT with secret key
            validate { credential ->
                // Validate JWT payload
                if (credential.payload.getClaim("userId").asInt() != null) { // Check for userId claim
                    JWTPrincipal(credential.payload) // Return principal if valid
                } else {
                    null // Return null if invalid
                }
            }
        }
    }

    // Configure routing (routes defined in a separate file)
    configureRouting()
}
