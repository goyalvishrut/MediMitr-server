package org.example.medimitr.orders.service

import org.example.medimitr.common.roundToTwoDecimalPlaces
import org.example.medimitr.models.NewOrder
import org.example.medimitr.models.Order
import org.example.medimitr.orders.repo.OrderRepository

class OrderService(
    private val orderRepository: OrderRepository,
) {
    suspend fun getOrdersByUser(userId: Int) = orderRepository.getOrdersByUser(userId)

    suspend fun createOrder(
        newOrder: NewOrder,
        userId: Int,
    ) = orderRepository.createOrder(
        order =
            newOrder.copy(
                totalAmount = newOrder.totalAmount.roundToTwoDecimalPlaces(),
            ),
        userProfileId = userId,
    )

    fun getOrderById(
        orderId: Int,
        userId: Int,
    ): Order? =
        orderRepository.getOrderById(
            orderId = orderId,
            userId = userId,
        )
}
