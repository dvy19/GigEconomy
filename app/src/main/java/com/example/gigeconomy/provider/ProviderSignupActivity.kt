package com.example.gigeconomy.provider

import android.R.attr.phoneNumber
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.gigeconomy.R
import com.example.gigeconomy.databinding.ActivityProviderSignupBinding
import com.example.gigeconomy.databinding.UserSignupBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class ProviderSignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProviderSignupBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProviderSignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.toProviderLogin.setOnClickListener{
            startActivity(Intent(this, ProviderLoginActivity::class.java))
        }

        binding.userMobileSubmit.setOnClickListener {
            val phoneInput = binding.providerMobile.text.toString().trim()

            val mail=binding.providerMail.text.toString().trim()
            val pass=binding.providerPassword.text.toString()

            if(mail.isEmpty() || pass.isEmpty() || phoneInput.isEmpty()){
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(pass.length<6){
                Toast.makeText(this, "Password should be at least 6 characters", Toast.LENGTH_SHORT).show()
            }
            auth.createUserWithEmailAndPassword(mail, pass)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Account Created", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(
                            this,
                            "Signup Failed: ${task.exception?.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

            if (phoneInput.isEmpty() || phoneInput.length < 10) {
                Toast.makeText(this, "Please Enter a Valid Mobile Number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val sanitizedPhone = phoneInput.replace("\\s|-".toRegex(), "")
            val fullPhone = "+1$sanitizedPhone"

            // Start OTP verification
            startPhoneNumberVerification(fullPhone)
        }
    }

    private fun startPhoneNumberVerification(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    // Optional: Auto verification
                    auth.signInWithCredential(credential).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this@ProviderSignupActivity, "Login Successful", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Toast.makeText(this@ProviderSignupActivity, "Verification Failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }

                override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                    // Pass verificationId to OTP activity
                    val intent = Intent(this@ProviderSignupActivity, ProviderOtpActivity::class.java)
                    intent.putExtra("verificationId", verificationId)
                    intent.putExtra("phoneNumber", phoneNumber)
                    startActivity(intent)
                }
            }).build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }
}
