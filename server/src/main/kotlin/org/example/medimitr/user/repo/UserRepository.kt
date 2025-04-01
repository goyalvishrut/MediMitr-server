package org.example.medimitr.user.repo

import org.example.medimitr.models.NewUser
import org.example.medimitr.models.User

interface UserRepository {
    suspend fun createUser(user: NewUser): User // Create a new user

    suspend fun findUserByEmail(email: String): User? // Find user by email

    suspend fun findUserByUserId(userId: Int): User?
}
