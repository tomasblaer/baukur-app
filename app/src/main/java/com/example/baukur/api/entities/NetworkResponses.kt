package com.example.baukur.api.entities

data class User(
    val id: Int,
    val email: String,
)

data class DefaultCategory(
    val id: Int,
    val name: String,
)

data class Category(
    val id: Int,
    val name: String,
    val description: String,
    val hidden: Boolean,
    val iconId: Int,
    val userId: Int,
    val defaultCategoryId: Int,
)