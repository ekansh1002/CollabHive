package com.example.getcollab.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.getcollab.R
import com.example.getcollab.activity.MessageActivity
import com.example.getcollab.databinding.PostLayoutBinding
import com.example.getcollab.model.PostModel
import com.example.getcollab.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class HomeAdapter(val context: Context, private val list: List<PostModel>) :
    RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    inner class HomeViewHolder(val binding: PostLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(PostLayoutBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val post = list[position]
        holder.binding.PostText.text = post.description1
        holder.binding.CardTitle.text = post.title
        holder.binding.usernameText.text = post.username ?: "Unknown User"

        // Set up the like button
        setupLikeButton(holder, post)

        holder.binding.commentButton.setOnClickListener {
            val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
            val postUserId = post.userId

            if (currentUserId != null && postUserId != null) {
                val sanitizedCurrentUserId = currentUserId.replace(Regex("[.@#\\$\\[\\]]"), "_")
                val sanitizedPostUserId = postUserId.replace(Regex("[.@#\\$\\[\\]]"), "_")
                val chatId = "${sanitizedCurrentUserId}--${sanitizedPostUserId}"

                val intent = Intent(context, MessageActivity::class.java).apply {
                    putExtra("chat_id", chatId)
                    putExtra("userId", postUserId)
                }
                context.startActivity(intent)
            } else {
                Log.d("HomeAdapterDebug", "User IDs are null")
            }
        }
    }

    private fun setupLikeButton(holder: HomeViewHolder, post: PostModel) {
        holder.binding.like.setOnClickListener {
            val scaleAnimation = AnimationUtils.loadAnimation(context, R.anim.heart_scale)
            holder.binding.like.startAnimation(scaleAnimation)

            holder.binding.like.setColorFilter(ContextCompat.getColor(context, R.color.red))

            saveLikedPost(post)
        }
    }

    private fun saveLikedPost(post: PostModel) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val likedPostsRef = FirebaseDatabase.getInstance().getReference("Users")
                .child(currentUser.uid)
                .child("likedPosts")
                .child(post.postId)

            likedPostsRef.setValue(post).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Post liked!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Failed to like post: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}





