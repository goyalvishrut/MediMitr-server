package org.example.medimitr.orders.repo

import org.example.medimitr.models.NewOrder
import org.example.medimitr.models.Order

interface OrderRepository {
    suspend fun createOrder(order: NewOrder, userProfileId: Int): Order // Create a new order

    suspend fun getOrdersByUser(userId: Int): List<Order> // Get orders for a user
}
