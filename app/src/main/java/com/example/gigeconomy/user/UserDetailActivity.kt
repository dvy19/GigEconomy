package com.example.gigeconomy.user

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gigeconomy.databinding.UserGetDetailsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserDetailActivity : AppCompatActivity() {
    private lateinit var binding: UserGetDetailsBinding
    private lateinit var firestore: FirebaseFirestore

    private lateinit  var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = UserGetDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance()
        auth= FirebaseAuth.getInstance()

        binding.submitUserDetails.setOnClickListener{

            val username=binding.userFullName.text.toString().trim()
            val homeAdd=binding.userHomeAddress.text.toString().trim()
            val landmark=binding.userLandmark.text.toString().trim()
            val pin=binding.userPinCode.text.toString().trim()
            val city=binding.userCity.text.toString().trim()


            if (username.isEmpty() || homeAdd.isEmpty() || city.isEmpty() || pin.isEmpty()) {
                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show()
               return@setOnClickListener
            }

            // Get current user
            val currentUser = auth.currentUser

            if (currentUser == null) {
                Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userData=UserDetails(

                fullName = username,
                homeAddress = homeAdd,
                landMark = landmark,
                City = city,
                pin = pin

            )

            firestore.collection("users")
                .document(currentUser.uid)
                .set(userData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Saved successfully!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }




        }




    }
}