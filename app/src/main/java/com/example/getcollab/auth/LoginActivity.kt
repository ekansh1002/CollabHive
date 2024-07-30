package com.example.getcollab.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.getcollab.MainActivity
import com.example.getcollab.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity: AppCompatActivity() {
    private lateinit var mAuth:FirebaseAuth
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuth = FirebaseAuth.getInstance()
        Log.d("LoginActivity", "onCreate: FirebaseAuth initialized")
        Log.d("LoginActivity", "onCreate: Binding initialized")
        binding.loginButton.setOnClickListener{
            val email = binding.username.text.toString()
            val password = binding.password.text.toString()
            if(email.isNotEmpty() && password.isNotEmpty()){
                loginUser(email,password)
            }else{
                Toast.makeText(this,"Fill the credentials",Toast.LENGTH_SHORT).show()
            }
        }
        binding.signuptext.setOnClickListener{
            startActivity(Intent(this,RegisterActivity::class.java))
        }


    }
    private fun loginUser(email:String,password:String){
        mAuth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){task ->
                if(task.isSuccessful){
                    Log.d("LoginActivity","signInwithEmail:success")
                    val user = mAuth.currentUser
                    Toast.makeText(this,"Authentication Successful.",Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,MainActivity::class.java))
                    finish()
                }else{
                    Log.w("LoginActivity","signInWithEmail:failure",task.exception)
                    Toast.makeText(this,"Authentication failed.",Toast.LENGTH_SHORT).show()
                }
            }
    }
}