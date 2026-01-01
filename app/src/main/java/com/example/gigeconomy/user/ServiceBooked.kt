package com.example.gigeconomy.user

data class ServiceBooked(
    val bookingId: String = "",
    val jobId: String = "",
    val providerId: String = "",
    val userId: String = "",
    val serviceType: String = "",
    val category: String = "",
    val rate: String = "",
    val city: String = "",
    var status: String = "requested", // requested, ongoing, completed, cancelled
    val bookingTime: Long = System.currentTimeMillis(),
    val completionTime: Long? = null
)
