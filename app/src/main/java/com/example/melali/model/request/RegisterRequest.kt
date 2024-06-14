package com.example.melali.model.request

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val confPassword: String,
    val phoneNumber: String
)
