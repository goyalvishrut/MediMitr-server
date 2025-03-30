package org.example.medimitr.models

import org.jetbrains.exposed.sql.Table // Import Exposed Table class

// Define Users table
object Users : Table() {
    // Auto-incrementing integer ID
    val id = integer("id").autoIncrement()
    val email = varchar("email", 255).uniqueIndex() // Unique email column (255 chars)
    val passwordHash = varchar("password_hash", 255) // Password hash column
    val name = varchar("name", 255) // Name column
    val address = varchar("address", 255) // Address column
    val phone = varchar("phone", 20) // Phone number column
    override val primaryKey = PrimaryKey(id) // Set ID as primary key
}

object Medicines : Table() { // Define Medicines table
    val id = integer("id").autoIncrement() // Auto-incrementing integer ID
    val name = varchar("name", 255) // Medicine name column
    val description = text("description") // Description column (unlimited text)
    val price = double("price") // Price column (double precision)
    val imageUrl = varchar("image_url", 255) // Image URL column
    override val primaryKey = PrimaryKey(id) // Set ID as primary key
}

object Orders : Table() { // Define Orders table
    val id = integer("id").autoIncrement() // Auto-incrementing integer ID
    val userId = integer("user_id").references(Users.id) // Foreign key to Users table
    val orderDate = long("order_date") // Order date and time
    val status = varchar("status", 50) // Order status (e.g., "Pending")
    val totalAmount = double("total_amount") // Total amount of the order
    val deliveryAddress = varchar("delivery_address", 255) // Delivery address
    val phone = varchar("phone", 20) // Contact phone number
    override val primaryKey = PrimaryKey(id) // Set ID as primary key
}

object OrderItems : Table() { // Define OrderItems table
    val id = integer("id").autoIncrement() // Auto-incrementing integer ID
    val orderId = integer("order_id").references(Orders.id) // Foreign key to Orders
    val medicineId = integer("medicine_id").references(Medicines.id) // Foreign key to Medicines
    val quantity = integer("quantity") // Quantity of medicine ordered
    val price = double("price") // Price per item
    override val primaryKey = PrimaryKey(id) // Set ID as primary key
}
