package com.example.gigeconomy.user

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.gigeconomy.R
import com.example.gigeconomy.databinding.ActivityConfirmedServiceLayoutBinding
import com.example.gigeconomy.provider.jobDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

//this is the layout which shows the details about the confirmed service.
class ConfirmedServiceLayout : AppCompatActivity() {

    private lateinit var binding: ActivityConfirmedServiceLayoutBinding
    private val db = FirebaseFirestore.getInstance()
    private val currentUser = FirebaseAuth.getInstance().currentUser

    private var job: jobDetails? = null   // 🔥 class-level variable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityConfirmedServiceLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val jobId = intent.getStringExtra("jobId")
        val providerId = intent.getStringExtra("providerId")

        if (jobId.isNullOrEmpty() || providerId.isNullOrEmpty()) {
            Toast.makeText(this, "Invalid job data", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        loadJobDetails(jobId, providerId)

    }

    private fun loadJobDetails(jobId: String, providerId: String) {

        db.collection("providers")
            .document(providerId)
            .collection("jobs")
            .document(jobId)
            .get()
            .addOnSuccessListener { document ->

                if (!document.exists()) {
                    Toast.makeText(this, "Job not found", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                val fetchedJob = document.toObject(jobDetails::class.java)

                fetchedJob?.let {
                    job = it   // 🔥 store in class-level variable

                    binding.serviceCnfCategory.text = it.category
                    binding.serviceCnfType.text = it.serviceType
                    binding.serviceCnfAddress.text = it.city
                    binding.serviceCnfDescription.text = it.serviceDes
                    binding.serviceCnfOwnerName.text = it.ownerName
                    binding.serviceCnfCompanyName.text = it.companyName
                    binding.serviceCnfRate.text = "₹${it.rate}"
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load job!", Toast.LENGTH_SHORT).show()
            }
    }
}