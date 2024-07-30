package com.example.getcollab.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.example.getcollab.R
import com.example.getcollab.databinding.FragmentPostBinding
import com.google.android.material.internal.ViewUtils.hideKeyboard

class PostFragment : Fragment() {
    private lateinit var binding: FragmentPostBinding


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


    }

    fun hideKeyboard() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        view?.let { v -> imm.hideSoftInputFromWindow(v.windowToken, 0) }
    }
}