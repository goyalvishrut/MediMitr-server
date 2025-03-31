package org.example.medimitr.orders.repo

import org.example.medimitr.models.NewOrder
import org.example.medimitr.models.Order
import org.example.medimitr.models.OrderItems
import org.example.medimitr.models.Orders
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class OrderRepositoryImpl : OrderRepository {
    override suspend fun createOrder(
        order: NewOrder,
        userProfileId: Int,
    ): Order =
        transaction {
            // Run in a transaction
            val generatedOrderId =
                Orders.insert {
                    // Insert into Orders table
                    it[userId] = userProfileId // Set user ID
                    it[orderDate] = System.currentTimeMillis() // Set current timestamp
                    it[status] = "Pending" // Set initial status
                    it[totalAmount] = order.totalAmount // Set total amount
                    it[deliveryAddress] = order.deliveryAddress // Set delivery address
                    it[phone] = order.phone // Set phone
                }[Orders.id] // Get the generated order ID

            order.items.forEach { item ->
                // Loop through order items
                OrderItems.insert {
                    // Insert into OrderItems table
                    it[orderId] = generatedOrderId // Link to order
                    it[medicineId] = item.medicineId // Set medicine ID
                    it[quantity] = item.quantity // Set quantity
                    it[price] = item.price // Set price
                }
            }

            Orders.select { Orders.id eq generatedOrderId }.single().let { row ->
                // Select the created order
                Order(
                    id = row[Orders.id],
                    userId = row[Orders.userId],
                    orderDate = row[Orders.orderDate],
                    status = row[Orders.status],
                    totalAmount = row[Orders.totalAmount],
                    deliveryAddress = row[Orders.deliveryAddress],
                    phone = row[Orders.phone],
                ) // Map to Order object
            }
        }

    override suspend fun getOrdersByUser(userId: Int): List<Order> =
        transaction {
            // Run in a transaction
            Orders.select { Orders.userId eq userId }.map { row ->
                // Select orders by user ID
                Order(
                    id = row[Orders.id],
                    userId = row[Orders.userId],
                    orderDate =
                        row[Orders.orderDate],
                    // Convert to Instant
                    status = row[Orders.status],
                    totalAmount = row[Orders.totalAmount],
                    deliveryAddress = row[Orders.deliveryAddress],
                    phone = row[Orders.phone],
                ) // Map to Order objects
            }
        }
}
