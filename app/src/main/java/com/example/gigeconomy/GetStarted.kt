package com.example.gigeconomy

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.gigeconomy.databinding.GetStartBinding
import com.example.gigeconomy.provider.ProviderSignupActivity
import com.example.gigeconomy.user.UserSignUpActivity

class GetStarted : AppCompatActivity() {

    private lateinit var binding: GetStartBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        supportActionBar?.hide()
        binding= GetStartBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        binding.toUser.setOnClickListener{
            startActivity(Intent(this, UserSignUpActivity::class.java))

        }

        binding.toProvider.setOnClickListener{
            startActivity(Intent(this, ProviderSignupActivity::class.java))

        }
    }
}