package com.example.gigeconomy.user

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.gigeconomy.R
import com.example.gigeconomy.databinding.UserOtpactivityBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class UserOTPActivity : AppCompatActivity() {

    private lateinit var binding: UserOtpactivityBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UserOtpactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        // Get the actual verificationId from intent
        val verificationId = intent.getStringExtra("verificationId")

        binding.userOtpSubmit.setOnClickListener {
            val otp = binding.userOTP.text.toString().trim()

            if (otp.isEmpty()) {
                Toast.makeText(this, "Please Enter OTP First", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (verificationId != null) {
                val credential = PhoneAuthProvider.getCredential(verificationId, otp)
                signInWithCredential(credential)
            } else {
                Toast.makeText(this, "Verification Id is null", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, UserDetailActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
