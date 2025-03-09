package com.example.baukur.api.network

import com.example.baukur.api.entities.CreateUserPayload
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface NetworkingService {
     @POST("user")
     suspend fun createUser(@Body payload: CreateUserPayload): Response<Unit>
}