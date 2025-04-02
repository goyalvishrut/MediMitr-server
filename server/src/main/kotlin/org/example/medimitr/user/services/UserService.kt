package org.example.medimitr.user.services

import org.example.medimitr.models.NewUser
import org.example.medimitr.models.User
import org.example.medimitr.user.mode.LoginCredentialsRequest
import org.example.medimitr.user.mode.UserRequest
import org.example.medimitr.user.repo.UserRepository
import org.mindrot.jbcrypt.BCrypt

class UserService(
    private val userRepository: UserRepository,
) {
    suspend fun createUser(userRequest: UserRequest): User {
        val passwordHash = getHashedPassword(userRequest.password)
        val user =
            userRepository.createUser(
                NewUser(
                    email = userRequest.email,
                    password = passwordHash,
                    name = userRequest.name,
                    address = userRequest.address,
                    phone = userRequest.phone,
                ),
            )
        return user
    }

    suspend fun findUserByEmail(loginCredentialsRequest: LoginCredentialsRequest): User? =
        userRepository.findUserByEmail(loginCredentialsRequest.email).let {
            println("vishrut-user" to it)
            if (it != null && verifyPassword(loginCredentialsRequest.password, it.passwordHash)) {
                User(
                    id = it.id,
                    email = it.email,
                    passwordHash = it.passwordHash,
                    name = it.name,
                    address = it.address,
                    phone = it.phone,
                )
            } else {
                null
            }
        }

    suspend fun getUserById(userId: Int): User? = userRepository.findUserByUserId(userId)

    private fun verifyPassword(
        password: String,
        hash: String,
    ): Boolean {
        return BCrypt.checkpw(password, hash) // Use BCrypt.checkpw
    }

    private fun getHashedPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt()) // Hash the password
    }

    suspend fun updateUserEmail(
        userId: Int,
        email: String,
    ): User? = userRepository.updateUserEmail(userId, email)

    suspend fun updateUserAddress(
        userId: Int,
        address: String,
    ): User? = userRepository.updateUserAddress(userId, address)

    suspend fun updateUserPhone(
        userId: Int,
        phone: String,
    ): User? = userRepository.updateUserPhone(userId, phone) // Update user phone

    suspend fun updateUserPassword(
        userId: Int,
        oldPassword: String,
        newPassword: String,
    ): User? =
        getUserById(userId)?.let {
            verifyPassword(oldPassword, it.passwordHash).let { isValid ->
                if (isValid) {
                    val hashedPassword = getHashedPassword(newPassword)
                    userRepository.updateUserPassword(userId, hashedPassword)
                } else {
                    null
                }
            }
        }
}
