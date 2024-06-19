package com.example.melali.model.request

data class SchedulingRequest(
    val idx_selected:List<Int>,
    val budget: Long,
    val days: Long,
    val lat_user: Double,
    val long_user: Double,
    val is_accessibility: Int
)
