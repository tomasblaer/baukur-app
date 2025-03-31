package com.example.baukur.api.network

import com.example.baukur.api.entities.Category
import com.example.baukur.api.entities.CreateCategoryPayload
import com.example.baukur.api.entities.CreateDefaultCategoriesPayload
import com.example.baukur.api.entities.CreateExpensePayload
import com.example.baukur.api.entities.CreateUserPayload
import com.example.baukur.api.entities.DefaultCategory
import com.example.baukur.api.entities.User
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part

interface NetworkingService {
     @POST("user")
     suspend fun createUser(@Body payload: CreateUserPayload): Response<User>
     @Multipart
     @POST("/login")
     suspend fun login(
          @Part("username") username: RequestBody,
          @Part("password") password: RequestBody
     ): Response<Unit>
     @POST("/logout")
     suspend fun logout(): Response<Unit>
     @GET("/user")
     suspend fun getUser(): Response<User>
     @PUT("/user")
     suspend fun updateUser(@Body payload: CreateUserPayload): Response<Unit>
     @GET("/categories/default")

     suspend fun getDefaultCategories(): Response<List<DefaultCategory>>
     @POST("/categories/default")
     suspend fun createDefaultCategories(@Body payload: CreateDefaultCategoriesPayload): Response<List<Category>>
     @GET("/categories")
     suspend fun getCategories(): Response<List<Category>>
     @POST("/categories")
     suspend fun createCategory(@Body payload: CreateCategoryPayload): Response<Category>

     @POST("/expenses")
     suspend fun createExpense(@Body payload: CreateExpensePayload): Response<Unit>

}