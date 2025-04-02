package org.example.medimitr.user.repo

import org.example.medimitr.models.NewUser
import org.example.medimitr.models.User
import org.example.medimitr.models.Users
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class UserRepositoryImpl : UserRepository {
    override suspend fun createUser(user: NewUser): User =
        try {
            transaction {
                // Run in a transaction
                Users
                    .insert {
                        // Insert into Users table
                        it[email] = user.email // Set email
                        it[passwordHash] = hashPassword(user.password) // Set hashed password
                        it[name] = user.name // Set name
                        it[address] = user.address // Set address
                        it[phone] = user.phone // Set phone
                    }.resultedValues!!
                    .first()
                    .let { row ->
                        // Get the inserted row
                        User(
                            row[Users.id],
                            row[Users.email],
                            row[Users.passwordHash],
                            row[Users.name],
                            row[Users.address],
                            row[Users.phone],
                        ) // Map to User object
                    }
            }
        } catch (e: Exception) {
            // Handle exception (e.g., log it)
            println("Error creating user: ${e.message}")
            throw e // Rethrow the exception
        }

    override suspend fun findUserByEmail(email: String): User? =
        try {
            transaction {
                // Run in a transaction
                Users.select { Users.email eq email }.singleOrNull()?.let { row ->
                    // Select user by email
                    User(
                        row[Users.id],
                        row[Users.email],
                        row[Users.passwordHash],
                        row[Users.name],
                        row[Users.address],
                        row[Users.phone],
                    ) // Map to User object
                }
            }
        } catch (e: Exception) {
            // Handle exception (e.g., log it)
            println("Error finding user by email: ${e.message}")
            null // Return null if an error occurs
        }

    override suspend fun findUserByUserId(userId: Int): User? =
        try {
            transaction {
                // Run in a transaction
                Users.select { Users.id eq userId }.singleOrNull()?.let { row ->
                    // Select user by ID
                    User(
                        row[Users.id],
                        row[Users.email],
                        row[Users.passwordHash],
                        row[Users.name],
                        row[Users.address],
                        row[Users.phone],
                    ) // Map to User object
                }
            }
        } catch (e: Exception) {
            // Handle exception (e.g., log it)
            println("Error finding user by ID: ${e.message}")
            null // Return null if an error occurs
        }

    override suspend fun updateUserEmail(
        userId: Int,
        email: String,
    ): User? {
        try {
            transaction {
                // Run in a transaction
                Users.update({ Users.id eq userId }) {
                    // Update user email
                    it[Users.email] = email // Set new email
                }
            }
            return findUserByUserId(userId) // Return updated user
        } catch (e: Exception) {
            // Handle exception (e.g., log it)
            println("Error updating user email: ${e.message}")
            return null // Return null if an error occurs
        }
    }

    override suspend fun updateUserAddress(
        userId: Int,
        address: String,
    ): User? =
        try {
            transaction {
                // Run in a transaction
                Users.update({ Users.id eq userId }) {
                    // Update user address
                    it[Users.address] = address // Set new address
                }
            }
            findUserByUserId(userId) // Return updated user
        } catch (e: Exception) {
            // Handle exception (e.g., log it)
            println("Error updating user address: ${e.message}")
            null // Return null if an error occurs
        }

    override suspend fun updateUserPhone(
        userId: Int,
        phone: String,
    ): User? =
        try {
            transaction {
                // Run in a transaction
                Users.update({ Users.id eq userId }) {
                    // Update user phone
                    it[Users.phone] = phone // Set new phone
                }
            }
            findUserByUserId(userId) // Return updated user
        } catch (e: Exception) {
            // Handle exception (e.g., log it)
            println("Error updating user phone: ${e.message}")
            null // Return null if an error occurs
        }

    override suspend fun updateUserPassword(
        userId: Int,
        hashedPassword: String,
    ): User? =
        try {
            transaction {
                // Run in a transaction
                Users.update({ Users.id eq userId }) {
                    // Update user password
                    it[Users.passwordHash] = hashedPassword // Set new hashed password
                }
            }
            findUserByUserId(userId) // Return updated user
        } catch (e: Exception) {
            // Handle exception (e.g., log it)
            println("Error updating user password: ${e.message}")
            null // Return null if an error occurs
        }

    private fun hashPassword(password: String): String = password // Placeholder for hashing (replace with BCrypt later)
}
