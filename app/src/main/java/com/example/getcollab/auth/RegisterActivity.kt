package com.example.getcollab.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.getcollab.MainActivity
import com.example.getcollab.databinding.ActivityRegisterBinding
import com.example.getcollab.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.auth.User

class RegisterActivity : AppCompatActivity(){
    private lateinit var binding : ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?){
         super.onCreate(savedInstanceState)

         binding = ActivityRegisterBinding.inflate(layoutInflater)
         setContentView(binding.root)

         auth = FirebaseAuth.getInstance()
         database = FirebaseDatabase.getInstance().reference.child("Users")

        binding.registerButton.setOnClickListener {
            val email = binding.emailId.text.toString().trim()
            val username = binding.userName.text.toString().trim()
            val password = binding.userPassword.text.toString().trim()

            if (validateFields(email, username, password)) {
                checkIfEmailExists(email) { emailExists ->
                    if (!emailExists) {
                        checkIfUsernameExists(username) { usernameExists ->
                            if (!usernameExists) {
                                registerUser(email, username, password)
                            } else {
                                Toast.makeText(this, "Username already taken", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(this, "Email ID already registered", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        binding.loginnowtext.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
    private fun validateFields(email: String, username: String, password: String): Boolean {
        if (email.isEmpty() || username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
    private fun checkIfEmailExists(email: String, callback: (Boolean) -> Unit) {
        database.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                callback(snapshot.exists())
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false)
            }
        })
    }
    private fun checkIfUsernameExists(username: String, callback: (Boolean) -> Unit) {
        database.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                callback(snapshot.exists())
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false)
            }
        })
    }
    private fun registerUser(email: String, username: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid ?: return@addOnCompleteListener
                    val user = UserModel(userId, email, username, password)

                    database.child(userId).setValue(user).addOnCompleteListener { dbTask ->
                        if (dbTask.isSuccessful) {
                            Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, "Failed to save user data", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}