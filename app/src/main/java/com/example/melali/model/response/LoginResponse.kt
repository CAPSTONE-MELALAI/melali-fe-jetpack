package com.example.melali.model.response

data class LoginResponse(
    val token: String,
    val uid: String,
    val username: String,
    val email: String,
    val phoneNumber: String,
    val age: Int,
    val gender: String,
    val indexUser: Long,
    val category: List<Int>
)
