package com.example.getcollab.model

data class LikedPostModel(
    val postId: String = "",         // Unique post ID
    val title: String = "",          // Post title
    val description1: String = "",   // First description
    val description2: String = "",
    var username: String? = "",
    val userId: String? = ""
)
