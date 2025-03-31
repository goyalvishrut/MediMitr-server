package org.example.medimitr.orders

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import org.example.medimitr.models.NewOrder
import org.example.medimitr.orders.service.OrderService
import org.koin.ktor.ext.inject

fun Application.configureOrdersRouting() {
    val orderService by inject<OrderService>()

    routing {
        authenticate {
            // Require authentication for routes below
            post("/orders") {
                // POST /orders endpoint
                val principal = call.principal<JWTPrincipal>() // Get JWT principal
                val userId =
                    principal?.payload?.getClaim("userId")?.asInt() // Extract userId from token
                        ?: return@post call.respond(HttpStatusCode.Unauthorized) // Return unauthorized if missing
                val newOrder = call.receive<NewOrder>() // Receive order and set userId
                val order = orderService.createOrder(newOrder, userId) // Create order
                call.respond(order) // Respond with created order
            }

            get("/orders") {
                // GET /orders endpoint
                val principal = call.principal<JWTPrincipal>() // Get JWT principal
                val userId =
                    principal?.payload?.getClaim("userId")?.asInt() // Extract userId
                        ?: return@get call.respond(HttpStatusCode.Unauthorized) // Return unauthorized if missing
                val orders = orderService.getOrdersByUser(userId) // Get user orders
                call.respond(orders) // Respond with order list
            }

            get("/orders/{orderId}") {
                // GET /orders/{orderId} endpoint
                val principal = call.principal<JWTPrincipal>()
                val userId =
                    principal?.payload?.getClaim("userId")?.asInt()
                        ?: return@get call.respond(HttpStatusCode.Unauthorized)

                val orderId =
                    call.parameters["orderId"]?.toIntOrNull() ?: return@get call.respond(
                        HttpStatusCode.BadRequest,
                        "Invalid orderId",
                    ) // Handle invalid orderId

                val order = orderService.getOrderById(orderId = orderId, userId = userId) // Get single order

                if (order == null) {
                    call.respond(HttpStatusCode.NotFound, "Order not found or unauthorized")
                } else {
                    call.respond(order)
                }
            }
        }
    }
}
