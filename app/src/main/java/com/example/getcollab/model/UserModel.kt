package com.example.getcollab.model

import android.os.Parcelable


class UserModel(
    val userId: String = "",        // Unique user ID
    val email: String = "",         // User email
    val username: String = "",      // User username
    val password: String = "",      // User password
    val posts: Map<String, PostModel>? = null,
    val chats: Map<String, Map<String, ChatMessage>>? = null,
    val likedPost: Map<String,LikedPostModel>? = null
)
data class ChatMessage(
    val currentTime: String = "",
    val senderId: String = "",
    val currentDate: String = "",
    val message: String = ""
)


