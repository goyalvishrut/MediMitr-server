package org.example.medimitr.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    // User entity
    val id: Int, // User ID
    val email: String, // User email
    val passwordHash: String, // Hashed password
    val name: String, // User name
    val address: String, // User address
    val phone: String, // User phone number
)

@Serializable
data class NewUser(
    // DTO for creating a new user
    val email: String, // Email
    val password: String, // Plaintext password (to be hashed)
    val name: String, // Name
    val address: String, // Address
    val phone: String, // Phone number
)

@Serializable
data class Medicine(
    // Medicine entity
    val id: Int, // Medicine ID
    val name: String, // Medicine name
    val description: String, // Description
    val price: Double, // Price
    val imageUrl: String, // Image URL
)

@Serializable
data class NewMedicine(
    // DTO for creating a new medicine
    val name: String, // Name
    val description: String, // Description
    val price: Double, // Price
    val imageUrl: String, // Image URL
)

@Serializable
data class Order(
    // Order entity
    val id: Int, // Order ID
    val userId: Int, // User who placed the order
    val orderDate: Long, // Date and time of order
    val status: String, // Order status
    val totalAmount: Double, // Total cost
    val deliveryAddress: String, // Delivery address
    val phone: String, // Contact phone
    val items: List<OrderItem>, // List of items in the order
)

@Serializable
data class OrderItem(
    // Order item entity
    val medicineId: Int, // Medicine ID
    val medicineName: String, // Medicine name
    val quantity: Int, // Quantity ordered
    val price: Double, // Price per item
)

@Serializable
data class NewOrder(
    // DTO for creating a new order
    val items: List<OrderItem>, // List of items in the order
    val totalAmount: Double, // Total cost
    val deliveryAddress: String, // Delivery address
    val phone: String, // Contact phone
)
