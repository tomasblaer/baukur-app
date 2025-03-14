package com.example.baukur.api.entities

data class CreateUserPayload(
    val email: String,
    val password: String,
)

data class CreateDefaultCategoriesPayload(
    val ids: List<Long>,
    val userId: Long,
)

data class CreateExpensePayload(
    val name: String,
    val amount: Double,
    val comment: String,
    val date: String,
    val categoryId: Int,
)