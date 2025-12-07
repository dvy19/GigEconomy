package com.example.gigeconomy.provider

import java.util.Date

data class ProviderDetails(
    val uid: String = "",
    val address: String = "",
    val companyName: String = "",
    val ownderName: String = "",
    val City: String = "",
    val pin: String = "",

    val timestamp: Date = Date()
)
