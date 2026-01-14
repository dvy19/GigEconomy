package com.example.gigeconomy.provider

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.gigeconomy.R
import com.example.gigeconomy.databinding.ActivityProviderLoginBinding
import com.example.gigeconomy.user.UserMainActivity
import com.google.firebase.auth.FirebaseAuth

class ProviderLoginActivity : AppCompatActivity() {

    private lateinit  var binding: ActivityProviderLoginBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding= ActivityProviderLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)
        auth= FirebaseAuth.getInstance()
        binding.userLogin.setOnClickListener{

            var email=binding.providerMail.text.toString()
            var pass=binding.providerPassword.text.toString()

            if(email.isEmpty() || pass.isEmpty()){
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            supportActionBar?.hide()

            var isPasswordVisible = false

            binding.ivToggle.setOnClickListener {
                isPasswordVisible = !isPasswordVisible

                if (isPasswordVisible) {
                    binding.providerPassword.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    binding.ivToggle.setImageResource(R.drawable.pass_eye)
                } else {
                    binding.providerPassword.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    binding.ivToggle.setImageResource(R.drawable.hidden)
                }

                // Move cursor to end
                binding.providerPassword.setSelection(binding.providerPassword.text.length)
            }

            auth= FirebaseAuth.getInstance()



            auth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()

                        // Redirect to Home Activity
                        startActivity(Intent(this, ProviderMainActivity::class.java))
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