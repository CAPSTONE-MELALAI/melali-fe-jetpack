package com.example.melali.model.response

data class SingleDestinationResponse(
    val address: String,
    val place: String,
    val is_accessibility: Int,
    val idx_category: Int,
    val rating: Double,
    val index: Int,
    val description: String,
    val n_reviews: Int,
    val url: String,
    val long: Double,
    val lat: Double,
    val uid: String,
    val Coordinate: String,
    val price: Long,
    val Is_accessibility: Boolean,
    val category: String,
)
