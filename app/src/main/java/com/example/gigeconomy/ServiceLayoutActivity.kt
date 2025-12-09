package com.example.gigeconomy

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gigeconomy.databinding.ServiceLayoutBinding
import com.example.gigeconomy.provider.jobDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ServiceLayoutActivity : AppCompatActivity() {

    private lateinit var binding: ServiceLayoutBinding

    private val db = FirebaseFirestore.getInstance()
    private val currentUser = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ServiceLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val jobId = intent.getStringExtra("jobId")

        if (jobId == null || currentUser == null) {
            Toast.makeText(this, "Error loading job!", Toast.LENGTH_SHORT).show()
            return
        }

        // Load job details on screen
        loadJobDetails(jobId)

        // BOOK BUTTON CLICK
        binding.bookService.setOnClickListener {

            // first get job details from FireStore so we can save the same object
            db.collection("providers")
                .document(intent.getStringExtra("providerId") ?: "")
                .collection("jobs")
                .document(jobId)
                .get()
                .addOnSuccessListener { doc ->
                    val job = doc.toObject(jobDetails::class.java)

                    if (job != null) {

                        // Save the SAME jobDetails object inside user's bookedServices
                        db.collection("users")
                            .document(currentUser.uid)
                            .collection("bookedServices")
                            .document(jobId)
                            .set(job)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Service booked successfully!", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to fetch job for booking!", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun loadJobDetails(jobId: String) {

        val providerId = intent.getStringExtra("providerId") ?: return

        db.collection("providers")
            .document(providerId)
            .collection("jobs")
            .document(jobId)
            .get()
            .addOnSuccessListener { document ->

                val job = document.toObject(jobDetails::class.java)

                if (job != null) {
                    binding.serviceDisCategory.text = job.category
                    binding.serviceDisType.text = job.serviceType
                    binding.serviceDisAddress.text = job.city
                    binding.serviceDisDescription.text = job.serviceDes
                    binding.serviceDisOwnerName.text = job.ownerName
                    binding.serviceDisRate.text = "₹${job.rate}"
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load job!", Toast.LENGTH_SHORT).show()
            }
    }
}
