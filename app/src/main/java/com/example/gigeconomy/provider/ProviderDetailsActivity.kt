package com.example.gigeconomy.provider

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.gigeconomy.R
import com.example.gigeconomy.databinding.ProviderGetDetailsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProviderDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ProviderGetDetailsBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {

        binding= ProviderGetDetailsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        firestore= FirebaseFirestore.getInstance()

        auth= FirebaseAuth.getInstance()


        supportActionBar?.hide()


        binding.submitProviderDetails.setOnClickListener{

            val ownerName=binding.ownerName.text.toString().trim()
            val address=binding.providerAddress.text.toString().trim()
            val pin=binding.providerPinCode.text.toString()
            val city=binding.providerCity.text.toString()
            val company=binding.companyName.text.toString()

            val currentProvider=auth.currentUser

            if (currentProvider == null) {
                Toast.makeText(this, "Not logged in", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val providerProfile= ProviderDetails(
                companyName = company,
                ownderName = ownerName,
                address = address,
                pin = pin,
                City = city,
            )


            firestore.collection("providers")
                .document(currentProvider.uid)
                .set(providerProfile)
                .addOnSuccessListener{

                    Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, ProviderMainActivity::class.java))

                }

                .addOnFailureListener{e->
                    Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()

                }

        }
    }
}