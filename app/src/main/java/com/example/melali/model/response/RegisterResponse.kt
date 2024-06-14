package com.example.melali.model.response

data class RegisterResponse(
    val token: String,
    val uid: String,
    val username: String,
    val email: String,
    val phoneNumber: String,
    val createdAt:String,
    val updatedAt:String
)
