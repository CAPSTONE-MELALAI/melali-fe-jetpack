package com.example.melali.model.response

data class ResponseWrapper <T>(
    val success:Boolean,
    val message:String,
    val data: T?
)
