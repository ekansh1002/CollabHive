package com.example.getcollab.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.getcollab.databinding.PostLayoutBinding
import com.example.getcollab.model.PostModel
import com.example.getcollab.model.UserModel

class HomeAdapter(val context:Context,val list : List<PostModel> ) : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {
    inner class HomeViewHolder(val binding : PostLayoutBinding)
        :RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(PostLayoutBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.binding.PostText.text = list[position].description1
        holder.binding.CardTitle.text = list[position].title
    }
}