package com.example.getcollab.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.getcollab.R
import com.example.getcollab.activity.PostDetailsActivity
import com.example.getcollab.databinding.LikedPostItemLayoutBinding
import com.example.getcollab.model.LikedPostModel
import com.example.getcollab.model.PostModel

class LikedPostAdapter(
    private val context: Context,
    private val list: List<LikedPostModel>
) : RecyclerView.Adapter<LikedPostAdapter.LikedPostViewHolder>() {

    inner class LikedPostViewHolder(val binding: LikedPostItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val post = list[adapterPosition]
                val intent = Intent(context, PostDetailsActivity::class.java).apply {
                    putExtra("postId", post.postId)
                    putExtra("username", post.username)
                    putExtra("title", post.title)
                    putExtra("description1", post.description1)
                    putExtra("description2", post.description2)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikedPostViewHolder {
        return LikedPostViewHolder(
            LikedPostItemLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: LikedPostViewHolder, position: Int) {
        val post = list[position]
        holder.binding.postTitle.text = post.title
        holder.binding.postDescription.text = post.description1
        holder.binding.postUsername.text = post.username
    }
}








