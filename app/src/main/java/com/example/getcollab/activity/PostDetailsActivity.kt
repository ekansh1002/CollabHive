package com.example.getcollab.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.getcollab.databinding.ActivityPostDetailsBinding

class PostDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve data from the intent
        val username = intent.getStringExtra("username") ?: "Unknown User"
        val title = intent.getStringExtra("title") ?: "No Title"
        val description1 = intent.getStringExtra("description1") ?: "No Description"
        val description2 = intent.getStringExtra("description2") ?: "No Description"

        // Set the data to the views
        binding.postUsername.text = "by " + username
        binding.postTitle.text = title
        binding.postDescription1.text = description1
        binding.postDescription2.text = description2
    }
}

