package com.example.getcollab.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.getcollab.adapter.MessageAdapter
import com.example.getcollab.databinding.ActivityMessageBinding
import com.example.getcollab.model.MessageModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*


class MessageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMessageBinding
    private lateinit var database: DatabaseReference
    private var receiverUserId: String? = null
    private var chatId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the chatId and userId from the intent
        chatId = intent.getStringExtra("chat_id")
        receiverUserId = intent.getStringExtra("userId")

        if (chatId != null) {
            getData(chatId!!)
        } else {
            Toast.makeText(this, "Chat ID is missing", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        binding.sendButton.setOnClickListener {
            val messageText = binding.yourMessage.text.toString().trim()
            if (messageText.isEmpty()) {
                Toast.makeText(this, "Enter Message", Toast.LENGTH_SHORT).show()
            } else {
                sendMessage(messageText)
            }
        }
    }

    private fun getData(chatId: String) {
        FirebaseDatabase.getInstance().getReference("chats").child(chatId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = arrayListOf<MessageModel>()
                    for (showSnap in snapshot.children) {
                        val data = showSnap.getValue(MessageModel::class.java)
                        if (data != null) {
                            list.add(data)
                        }
                    }
                    // Ensure the adapter is updated with new data
                    binding.recyclerView2.adapter = MessageAdapter(this@MessageActivity, list)
                    // Scroll to the bottom to show the latest message
                    binding.recyclerView2.scrollToPosition(list.size - 1)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@MessageActivity, error.message, Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun sendMessage(message: String) {
        val senderId = FirebaseAuth.getInstance().currentUser?.uid

        if (senderId == null || receiverUserId == null || chatId == null) {
            Toast.makeText(this, "Cannot send message due to missing IDs", Toast.LENGTH_SHORT).show()
            return
        }

        val map = hashMapOf(
            "message" to message,
            "senderId" to senderId,
            "currentTime" to SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()),
            "currentDate" to SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        )

        val reference = FirebaseDatabase.getInstance().getReference("chats").child(chatId!!)
        val messageRef = reference.push()

        messageRef.setValue(map).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Message sent", Toast.LENGTH_SHORT).show()
                binding.yourMessage.text!!.clear()
            } else {
                Log.e("FirebaseError", "Error: ${task.exception?.message}")
                Toast.makeText(this, "Error sending message", Toast.LENGTH_SHORT).show()
            }
        }
    }
}



