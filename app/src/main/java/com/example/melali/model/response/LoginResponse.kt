package com.example.melali.model.response

data class LoginResponse(
    val token: String,
    val uid: String,
    val username: String,
    val email: String,
    val phoneNumber: String
)
