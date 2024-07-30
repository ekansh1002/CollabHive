package com.example.getcollab.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var manager: CardStackLayoutManager
    private lateinit var list: ArrayList<PostModel>
    private lateinit var adapter: HomeAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        getSampleData()
        return binding.root
    }

    private fun init() {
        manager = CardStackLayoutManager(requireContext(), object : CardStackListener {
            override fun onCardDragging(direction: Direction?, ratio: Float) {
                // Handle card dragging
            }

            override fun onCardSwiped(direction: Direction?) {
                if (manager.topPosition == list.size - 1) {
                    list.addAll(list)
                    adapter.notifyDataSetChanged()
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
        manager.setSwipeThreshold(0.3f)
        manager.setMaxDegree(20.0f)
        manager.setDirections(Direction.HORIZONTAL)
        manager.setCanScrollHorizontal(true)
        manager.setCanScrollVertical(false)

        binding.cardStackView.layoutManager = manager
        binding.cardStackView.itemAnimator = DefaultItemAnimator()

        binding.cardStackView.adapter = HomeAdapter(requireContext(), list)
        adapter = HomeAdapter(requireContext(),list)
        binding.cardStackView.adapter = adapter
    }

    private fun getData() {
        FirebaseDatabase.getInstance().getReference("Users")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        list = arrayListOf()
                        for (data in snapshot.children) {
                            val model = data.getValue(PostModel::class.java)
                            if (model != null) {
                                list.add(model)
                            }
                        }
                        list.shuffle()
                        init()
                        binding.cardStackView.adapter = HomeAdapter(requireContext(), list)

                    } else {
                        Toast.makeText(requireContext(), "No data available", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
                }
            })
    }
    private fun getSampleData() {
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
    }
}

