package com.example.getcollab.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.getcollab.R
import com.example.getcollab.auth.LoginActivity
import com.google.firebase.auth.FirebaseAuth


class ProfileFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find the logout button and set a click listener
        view.findViewById<View>(R.id.logoutButton).setOnClickListener {
            signOut()
        }
    }
    private fun signOut() {
        // Sign out from Firebase
        FirebaseAuth.getInstance().signOut()

        // Redirect to Login Activity
        val intent = Intent(activity, LoginActivity::class.java)
        startActivity(intent)
        activity?.finish()  // Optionally, finish the current activity to remove it from the back stack
    }



}