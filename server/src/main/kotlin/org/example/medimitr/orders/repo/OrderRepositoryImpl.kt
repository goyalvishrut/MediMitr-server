package org.example.medimitr.orders.repo

import org.example.medimitr.models.Medicines
import org.example.medimitr.models.NewOrder
import org.example.medimitr.models.Order
import org.example.medimitr.models.OrderItem
import org.example.medimitr.models.OrderItems
import org.example.medimitr.models.Orders
import org.jetbrains.exposed.sql.and
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
                    items =
                        OrderItems
                            .select { OrderItems.orderId eq generatedOrderId }
                            .map { itemRow ->
                                // Select order items
                                OrderItem(
                                    medicineId = itemRow[OrderItems.medicineId],
                                    quantity = itemRow[OrderItems.quantity],
                                    price = itemRow[OrderItems.price],
                                    medicineName =
                                        itemRow[OrderItems.medicineId].let { medicineId ->
                                            // Get medicine name
                                            Medicines
                                                .select { Medicines.id eq medicineId }
                                                .single()[Medicines.name]
                                        },
                                )
                            }, // Map to OrderItems objects
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
                    items =
                        OrderItems
                            .select { OrderItems.orderId eq row[Orders.id] }
                            .map { itemRow ->
                                // Select order items
                                OrderItem(
                                    medicineId = itemRow[OrderItems.medicineId],
                                    quantity = itemRow[OrderItems.quantity],
                                    price = itemRow[OrderItems.price],
                                    medicineName =
                                        itemRow[OrderItems.medicineId].let { medicineId ->
                                            // Get medicine name
                                            Medicines
                                                .select { Medicines.id eq medicineId }
                                                .single()[Medicines.name]
                                        },
                                )
                            }, // Map to OrderItems objects,
                ) // Map to Order objects
            }
        }

    override fun getOrderById(
        orderId: Int,
        userId: Int,
    ): Order? =
        transaction {
            // Run in a transaction
            Orders
                .select { (Orders.id eq orderId) and (Orders.userId eq userId) }
                .map { row ->
                    // Map to Order object
                    Order(
                        id = row[Orders.id],
                        userId = row[Orders.userId],
                        orderDate = row[Orders.orderDate],
                        status = row[Orders.status],
                        totalAmount = row[Orders.totalAmount],
                        deliveryAddress = row[Orders.deliveryAddress],
                        phone = row[Orders.phone],
                        items =
                            OrderItems
                                .select { OrderItems.orderId eq row[Orders.id] }
                                .map { itemRow ->
                                    // Select order items
                                    OrderItem(
                                        medicineId = itemRow[OrderItems.medicineId],
                                        quantity = itemRow[OrderItems.quantity],
                                        price = itemRow[OrderItems.price],
                                        medicineName =
                                            itemRow[OrderItems.medicineId].let { medicineId ->
                                                // Get medicine name
                                                Medicines
                                                    .select { Medicines.id eq medicineId }
                                                    .single()[Medicines.name]
                                            },
                                        // Get medicine name
                                    )
                                }, // Map to OrderItems objects,
                    )
                }.singleOrNull() // Return single order or null if not found
        }
}
