package com.example.getcollab.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.getcollab.R
import com.example.getcollab.activity.PostDetailsActivity
import com.example.getcollab.adapter.LikedPostAdapter
import com.example.getcollab.databinding.FragmentLikeBinding
import com.example.getcollab.model.LikedPostModel
import com.example.getcollab.model.PostModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LikeFragment : Fragment() {

    private lateinit var binding: FragmentLikeBinding
    private lateinit var adapter: LikedPostAdapter

    private val likedPosts = mutableListOf<LikedPostModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLikeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = LikedPostAdapter(requireContext(), likedPosts)
        binding.likedRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.likedRecyclerView.adapter = adapter

        loadLikedPosts()
    }

    private fun loadLikedPosts() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val likedPostsRef = FirebaseDatabase.getInstance().getReference("Users")
                .child(currentUser.uid)
                .child("likedPosts")

            likedPostsRef.orderByKey().addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    likedPosts.clear()
                    for (postSnapshot in snapshot.children) {
                        val likedPost = postSnapshot.getValue(LikedPostModel::class.java)
                        if (likedPost != null) {
                            likedPosts.add(likedPost)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

}



