package com.example.getcollab.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import com.example.getcollab.R
import com.example.getcollab.adapter.HomeAdapter
import com.example.getcollab.databinding.FragmentHomeBinding
import com.example.getcollab.model.PostModel
import com.example.getcollab.model.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction
import com.example.getcollab.model.UserModel as UserModel1

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var manager: CardStackLayoutManager
    private lateinit var adapter: HomeAdapter

    private val list = mutableListOf<PostModel>() // Use mutable list directly

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = HomeAdapter(requireContext(), list)
        binding.cardStackView.adapter = adapter

        init()
        getData()
    }

    private fun init() {
        manager = CardStackLayoutManager(requireContext(), object : CardStackListener {
            override fun onCardDragging(direction: Direction?, ratio: Float) {
                // Handle card dragging
            }

            override fun onCardSwiped(direction: Direction?) {
                if (manager.topPosition == list.size) {
                    manager.topPosition = 0
                    // Ensure adapter is set correctly
                    binding.cardStackView.adapter = adapter
                }
            }

            override fun onCardRewound() {
                // Handle card rewind
            }

            override fun onCardCanceled() {
                // Handle card cancel
            }

            override fun onCardAppeared(view: View?, position: Int) {
                // Handle card appeared
            }

            override fun onCardDisappeared(view: View?, position: Int) {
                // Handle card disappeared
            }
        })

        manager.setVisibleCount(3)
        manager.setTranslationInterval(8.0f)
        manager.setScaleInterval(0.95f)
        manager.setSwipeThreshold(0.2f) // Lowered threshold to ensure full swipe off screen
        manager.setMaxDegree(20.0f)
        manager.setDirections(Direction.HORIZONTAL)
        manager.setCanScrollHorizontal(true)
        manager.setCanScrollVertical(false)
        manager.setOverlayInterpolator(LinearInterpolator()) // Smooth interpolation for better swipe effect

        binding.cardStackView.layoutManager = manager
        binding.cardStackView.itemAnimator = DefaultItemAnimator()
    }

    private fun getData() {
        val userRef = FirebaseDatabase.getInstance().getReference("Users")

        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (isAdded && context != null) {
                    if (snapshot.exists()) {
                        val postsList = mutableListOf<PostModel>()
                        for (userSnapshot in snapshot.children) {
                            val postsSnapshot = userSnapshot.child("posts") // Assuming posts are under a "Posts" node in each user node

                            for (postSnapshot in postsSnapshot.children) {
                                val postMap = postSnapshot.value as? Map<String, Any> ?: continue
                                val postModel = PostModel(
                                    postId = postMap["postId"] as? String ?: "",
                                    title = postMap["title"] as? String ?: "",
                                    description1 = postMap["description1"] as? String ?: "",
                                    description2 = postMap["description2"] as? String ?: "",
                                    username = userSnapshot.child("username").getValue(String::class.java), // Assuming UserModel has a username property
                                    userId = postMap["userId"] as? String
                                )
                                postsList.add(postModel)
                            }
                        }
                        postsList.shuffle()
                        list.clear()
                        list.addAll(postsList)
                        adapter.notifyDataSetChanged() // Notify adapter of data change
                    } else {
                        activity?.runOnUiThread {
                            Toast.makeText(
                                requireContext(),
                                "No data available",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}



   /* private fun getSampleData() {
        list = arrayListOf(
            PostModel(
                postId = "1",
                title = "Exploring Kotlin",
                description1 = "An introduction to Kotlin programming language.",
                description2 = "This description will not be shown in the adapter."
            ),
            PostModel(
                postId = "2",
                title = "Android Development",
                description1 = "Best practices for Android development using Kotlin.",
                description2 = "This description will not be shown in the adapter."
            ),
            PostModel(
                postId = "3",
                title = "Firebase Integration",
                description1 = "How to integrate Firebase into your Android app.",
                description2 = "This description will not be shown in the adapter."
            ),
            PostModel(
                postId = "4",
                title = "UI/UX Design",
                description1 = "Tips for designing user-friendly interfaces.",
                description2 = "This description will not be shown in the adapter."
            ),
            PostModel(
                postId = "5",
                title = "Jetpack Compose",
                description1 = "Getting started with Jetpack Compose for UI development.",
                description2 = "This description will not be shown in the adapter."
            )
        )
        list.shuffle()
        init()
        //binding.cardStackView.adapter = HomeAdapter(requireContext(), list)
    }*/

