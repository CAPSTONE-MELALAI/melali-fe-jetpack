package com.example.melali.model.response

data class UserResponse(
    val uid:String,
    val phoneNumber:String,
    val indexUser: Long,
    val category: List<Int>,
    val email: String,
    val username: String
)
