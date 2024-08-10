package com.example.getcollab.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.getcollab.R
import com.example.getcollab.databinding.FragmentPostBinding
import com.example.getcollab.model.PostModel
import com.google.android.material.internal.ViewUtils.hideKeyboard
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class PostFragment : Fragment() {
    private lateinit var binding: FragmentPostBinding
    private lateinit var firebaseRef: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPostBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up focus change listeners for the EditText fields
        firebaseRef = FirebaseDatabase.getInstance().getReference("Users")
        auth = FirebaseAuth.getInstance()
        binding.root.setOnTouchListener { _, _ ->
            hideKeyboard()
            false
        }
        binding.inputField1.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                hideKeyboard()
            }
        }
        binding.inputField2.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                hideKeyboard()
            }
        }

        binding.inputField3.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                hideKeyboard()
            }
        }
        binding.postButton.setOnClickListener {
            val title = binding.inputField1.text.toString().trim()
            val description1 = binding.inputField2.text.toString().trim()
            val description2 = binding.inputField3.text.toString().trim()

            if (title.isNotEmpty() && description1.isNotEmpty() && description2.isNotEmpty()) {
                savePostToFirebase(title, description1, description2)
            } else {
                Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }


    }

    fun hideKeyboard() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        view?.let { v -> imm.hideSoftInputFromWindow(v.windowToken, 0) }
    }
    private fun savePostToFirebase(title: String, description1: String, description2: String) {
        val currentUser = auth.currentUser
        currentUser?.let {
            val userId = it.uid
            val username = it.displayName
            val postId = firebaseRef.child(userId).child("posts").push().key ?: return
            val post = PostModel(postId, title, description1, description2,username,userId = userId)
            Log.d("PostFragmentDebug", "Saving post with userId: $userId and username: $username")
            firebaseRef.child(userId).child("posts").child(postId).setValue(post)
                .addOnSuccessListener {
                    activity?.runOnUiThread{Toast.makeText(context, "Posted", Toast.LENGTH_SHORT).show()
                    }
                    binding.inputField1.text?.clear()
                    binding.inputField2.text?.clear()
                    binding.inputField3.text?.clear()

                }
                .addOnFailureListener { e ->
                    Toast.makeText(context,"Try Again",Toast.LENGTH_SHORT).show()
                }
        }
    }

}