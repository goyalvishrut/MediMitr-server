package org.example.medimitr.user.repo

import org.example.medimitr.models.NewUser
import org.example.medimitr.models.User

interface UserRepository {
    suspend fun createUser(user: NewUser): User // Create a new user

    suspend fun findUserByEmail(email: String): User? // Find user by email

    suspend fun findUserByUserId(userId: Int): User?

    suspend fun updateUserEmail(
        userId: Int,
        email: String,
    ): User?

    suspend fun updateUserAddress(
        userId: Int,
        address: String,
    ): User?

    suspend fun updateUserPhone(
        userId: Int,
        phone: String,
    ): User?

    suspend fun updateUserPassword(
        userId: Int,
        hashedPassword: String,
    ): User?
}
