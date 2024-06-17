package com.example.melali.model.response

data class SingleDestinationMLResponse(
    val idx_place: Long,
    val place: String,
    val url: String,
    val address: String,
    val is_accessbility: Int,
    val rating: Double,
    val n_reviews: Long,
    val price: Long,
    val category: String,
    val description: String
)
