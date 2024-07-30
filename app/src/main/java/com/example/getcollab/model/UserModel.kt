package com.example.getcollab.model



class UserModel(
    val userId: String = "",        // Unique user ID
    val email: String = "",         // User email
    val username: String = "",      // User username
    val password: String = "",      // User password
    val posts: List<PostModel>? = null  // List of posts made by the user
)



