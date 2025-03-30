package org.example.medimitr.orders.service

import org.example.medimitr.models.NewOrder
import org.example.medimitr.orders.repo.OrderRepository

class OrderService(
    private val orderRepository: OrderRepository,
) {
    suspend fun getOrdersByUser(userId: Int) = orderRepository.getOrdersByUser(userId)

    suspend fun createOrder(newOrder: NewOrder) = orderRepository.createOrder(newOrder)
}
