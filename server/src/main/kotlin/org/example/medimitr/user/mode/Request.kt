package org.example.medimitr.user.mode

import kotlinx.serialization.Serializable

@Serializable
data class UserRequest(
    val email: String,
    val password: String, // Plaintext, to be hashed before saving
    val name: String,
    val address: String,
    val phone: String,
)

@Serializable
data class LoginCredentialsRequest(
    val email: String,
    val password: String,
) // DTO for login request

@Serializable
data class PasswordChange(
    val oldPassword: String,
    val newPassword: String,
)
