package com.kmose.viewbinding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kmose.viewbinding.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSubmit.setOnClickListener {
            displayGreeting()
        }
    }

    private fun displayGreeting() {

        binding.greetingTextView.text = "Hello! " + binding.etNameText.text
    }
}