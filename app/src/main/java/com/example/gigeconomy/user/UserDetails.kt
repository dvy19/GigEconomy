package com.example.gigeconomy.user

import java.util.Date

data class UserDetails(
    val uid: String = "",
    val fullName: String = "",
    val homeAddress: String = "",
    val landMark: String = "",
    val City: String = "",
    val pin: String = "",
    val profileImageUrl: String? = null,   // 👈 OPTIONAL

    val timestamp: Date = Date()
)
