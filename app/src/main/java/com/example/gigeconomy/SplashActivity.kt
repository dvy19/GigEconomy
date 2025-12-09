package com.example.gigeconomy

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.gigeconomy.provider.ProviderMainActivity
import com.example.gigeconomy.user.UserMainActivity
import com.example.gigeconomy.user.UserSignUpActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SplashActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val currentUser = auth.currentUser

        if (currentUser == null) {
            // Not logged in → go to signup/login
            startActivity(Intent(this, GetStarted::class.java))
            finish()
            return
        }

        val uid = currentUser.uid

        // First check USERS collection
        db.collection("users").document(uid).get()
            .addOnSuccessListener { userSnap ->
                if (userSnap.exists()) {
                    // UID found in users → OPEN USER DASHBOARD
                    startActivity(Intent(this, UserMainActivity::class.java))
                    finish()
                } else {
                    // Not found in users → check providers
                    db.collection("providers").document(uid).get()
                        .addOnSuccessListener { providerSnap ->
                            if (providerSnap.exists()) {
                                // UID found in providers → OPEN PROVIDER DASHBOARD
                                startActivity(Intent(this, ProviderMainActivity::class.java))
                                finish()
                            } else {
                                // UID exists in Auth but NO ROLE → go to role selection
                                startActivity(Intent(this, GetStarted::class.java))
                                finish()
                            }
                        }
                }
            }
    }
}