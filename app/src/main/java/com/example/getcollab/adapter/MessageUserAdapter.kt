package com.example.getcollab.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.getcollab.R
import com.example.getcollab.databinding.UserItemLayoutBinding
import com.example.getcollab.model.PostModel
import com.example.getcollab.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView

class MessageUserAdapter(
    private val context: Context,
    private val userList: List<UserModel>,
    private val onUserClickListener: OnUserClickListener
) : RecyclerView.Adapter<MessageUserAdapter.UserViewHolder>() {

    interface OnUserClickListener {
        fun onUserClick(userId: String)
    }

    inner class UserViewHolder(val binding: UserItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val userId = userList[adapterPosition].userId
                onUserClickListener.onUserClick(userId)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = UserItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.binding.usernameText.text = user.username
    }

    override fun getItemCount(): Int = userList.size
}





