package com.example.baukur.api.entities

data class User(
    val id: Int,
    val email: String,
)

data class DefaultCategory(
    val id: Int,
    val name: String,
)

data class Expense(
    val id: Int,
    val name: String,
    val comment: String,
    val amount: Double,
    val date: String,
    val categoryId: Int,
    val userId: Int,
)

data class Category(
    val id: Int,
    val name: String,
    val description: String,
    val hidden: Boolean,
    val iconId: Int,
    val userId: Int,
    val defaultCategoryId: Int,
    val expenses: List<Expense>,
)