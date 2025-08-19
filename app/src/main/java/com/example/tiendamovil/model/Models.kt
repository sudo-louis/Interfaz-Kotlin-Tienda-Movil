package com.example.tiendamovil.model

data class ApiMessage(val message: String)
data class LoginResponse(val message: String, val token: String, val user: User)
data class User(val id: Int, val name: String, val email: String)

data class Product(val id: Int, val image: String?, val name: String, val price: Double)

data class RegisterRequest(val name: String, val email: String, val password: String)
data class LoginRequest(val email: String, val password: String)
data class CartCreateRequest(val product_id: Int, val quantity: Int = 1)

data class CartItem(
    val id: Int,
    val product_id: Int,
    val name: String?,
    val image: String?,
    val price: Double?,
    val quantity: Int,
    val subtotal: Double?
)