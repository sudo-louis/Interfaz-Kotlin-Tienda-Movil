package com.example.tiendamovil.net

import com.example.tiendamovil.model.*
import retrofit2.http.*

interface ApiService {
    @POST("register")
    suspend fun register(@Body body: RegisterRequest): ApiMessage

    @POST("login")
    suspend fun login(@Body body: LoginRequest): LoginResponse

    @POST("logout")
    suspend fun logout(): ApiMessage

    @GET("products")
    suspend fun products(): List<Product>

    @POST("cart")
    suspend fun addToCart(@Body body: CartCreateRequest): ApiMessage

    @GET("cart")
    suspend fun getCart(): List<CartItem>
}
