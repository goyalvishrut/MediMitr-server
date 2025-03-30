package org.example.medimitr.user.repo

import org.example.medimitr.models.NewUser
import org.example.medimitr.models.User
import org.example.medimitr.models.Users
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class UserRepositoryImpl : UserRepository {
    override suspend fun createUser(user: NewUser): User =
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

    override suspend fun findUserByEmail(email: String): User? =
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

    private fun hashPassword(password: String): String = password // Placeholder for hashing (replace with BCrypt later)
}
