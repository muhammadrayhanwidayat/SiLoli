package com.example.siloli.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val username: String
)

@Serializable
data class LoginResponse(
    val token: String,
    val user: User
)

@Serializable
data class RegisterRequest(
    val username: String,
    val password: String
)

@Serializable
data class LoginRequest(
    val username: String,
    val password: String
)

@Serializable
data class GenericResponse(
    val status: String? = null,
    val message: String? = null,
    val error: String? = null,
    val id: Int? = null
)
