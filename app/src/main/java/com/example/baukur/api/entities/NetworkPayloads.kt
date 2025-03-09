package com.example.baukur.api.entities

data class CreateUserPayload(
    val email: String,
    val password: String,
)