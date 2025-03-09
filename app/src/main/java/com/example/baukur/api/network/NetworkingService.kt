package com.example.baukur.api.network

import com.example.baukur.api.entities.CreateUserPayload
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface NetworkingService {
     @POST("user")
     suspend fun createUser(@Body payload: CreateUserPayload): Response<Unit>
     @Multipart
     @POST("/login")
     suspend fun login(
          @Part("username") username: RequestBody,
          @Part("password") password: RequestBody
     ): Response<Unit>


}