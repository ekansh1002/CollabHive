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
import com.example.getcollab.activity.MessageActivity
import com.example.getcollab.adapter.MessageUserAdapter
import com.example.getcollab.databinding.FragmentMsgBinding
import com.example.getcollab.model.UserModel
import com.example.getcollab.ui.HomeFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale




class MsgFragment : Fragment(), MessageUserAdapter.OnUserClickListener {

    private lateinit var binding: FragmentMsgBinding
    private lateinit var adapter: MessageUserAdapter
    private val userList = mutableListOf<UserModel>()
    private val userLastMessageTimeMap = mutableMapOf<String, Long>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMsgBinding.inflate(inflater, container, false)

        adapter = MessageUserAdapter(requireContext(), userList, this)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        fetchChats()
        return binding.root
    }

    override fun onUserClick(userId: String) {
        val chatId = createChatId(userId)
        val intent = Intent(requireContext(), MessageActivity::class.java).apply {
            putExtra("chat_id", chatId)
            putExtra("userId", userId)
        }
        startActivity(intent)
    }

    private fun createChatId(otherUserId: String): String {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        return if (currentUserId != null) {
            val sanitizedCurrentUserId = currentUserId.replace(Regex("[.@#\\$\\[\\]]"), "_")
            val sanitizedOtherUserId = otherUserId.replace(Regex("[.@#\\$\\[\\]]"), "_")
            "${sanitizedCurrentUserId}--${sanitizedOtherUserId}"
        } else {
            ""
        }
    }

    private fun fetchChats() {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUserId != null) {
            val chatRef = FirebaseDatabase.getInstance().getReference("chats")
            chatRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val chatUsers = mutableSetOf<String>()
                    for (chatSnapshot in snapshot.children) {
                        val chatId = chatSnapshot.key ?: continue
                        val userIds = chatId.split("--")
                        if (userIds.size == 2) {
                            if (userIds[0] == currentUserId) {
                                chatUsers.add(userIds[1])
                                fetchLastMessageTime(chatSnapshot, userIds[1])
                            } else if (userIds[1] == currentUserId) {
                                chatUsers.add(userIds[0])
                                fetchLastMessageTime(chatSnapshot, userIds[0])
                            }
                        }
                    }
                    fetchUserDetails(chatUsers)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("FirebaseError", "Error: ${error.message}")
                }
            })
        }
    }

    private fun fetchLastMessageTime(chatSnapshot: DataSnapshot, userId: String) {
        var lastMessageTime = 0L
        val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
        for (messageSnapshot in chatSnapshot.children) {
            val currentDate = messageSnapshot.child("currentDate").getValue(String::class.java) ?: ""
            val currentTime = messageSnapshot.child("currentTime").getValue(String::class.java) ?: ""
            val dateTimeString = "$currentDate $currentTime"
            try {
                val messageTime = dateFormat.parse(dateTimeString)?.time ?: 0L
                lastMessageTime = maxOf(lastMessageTime, messageTime)
            } catch (e: ParseException) {
                Log.e("DateParseError", "Error parsing date: ${e.message}")
            }
        }
        userLastMessageTimeMap[userId] = lastMessageTime
        sortAndNotify()
    }

    private fun sortAndNotify() {
        userList.sortWith(compareByDescending { userLastMessageTimeMap[it.userId] ?: 0L })
        adapter.notifyDataSetChanged()
    }

    private fun fetchUserDetails(userIds: Set<String>) {
        val userRef = FirebaseDatabase.getInstance().getReference("Users")
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (userSnapshot in snapshot.children) {
                    val user = userSnapshot.getValue(UserModel::class.java)
                    if (user != null && userIds.contains(user.userId)) {
                        userList.add(user)
                    }
                }
                sortAndNotify()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseError", "Error: ${error.message}")
            }
        })
    }
}







