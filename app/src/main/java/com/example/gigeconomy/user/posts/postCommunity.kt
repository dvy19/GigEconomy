package com.example.gigeconomy.user.posts



data class postCommunity(
    var postId: String = "",
    var userId: String = "",
    var username: String = "",
    var company: String = "",
    var content: String = "",
    var like: Int = 0,
    var timestamp: Long = System.currentTimeMillis()
)
