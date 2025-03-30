package org.example.medimitr

import io.ktor.server.application.Application
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import org.example.medimitr.medicines.configureMedicineRouting
import org.example.medimitr.orders.configureOrdersRouting
import org.example.medimitr.user.configureUserRouting

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Ktor: ${Greeting().greet()}")
        }
    }

    configureUserRouting()
    configureMedicineRouting()
    configureOrdersRouting()
}
