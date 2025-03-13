package com.example.baukur.api.entities

data class CreateUserPayload(
    val email: String,
    val password: String,
)

data class CreateDefaultCategoriesPayload(
    val ids: List<Long>,
    val userId: Long,
)