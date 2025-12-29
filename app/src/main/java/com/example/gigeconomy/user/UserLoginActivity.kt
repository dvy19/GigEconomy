package com.example.gigeconomy.user

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.gigeconomy.R
import com.example.gigeconomy.databinding.ActivityUserLoginBinding
import com.google.firebase.auth.FirebaseAuth

class UserLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserLoginBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding= ActivityUserLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.userLogin.setOnClickListener{

            var email=binding.userMail.text.toString()
            var pass=binding.userPassword.text.toString()

            auth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()

                        // Redirect to Home Activity
                        startActivity(Intent(this, UserMainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(
                            this,
                            "Login Failed: ${task.exception?.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }

    }
}