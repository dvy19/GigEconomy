package com.example.gigeconomy

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gigeconomy.databinding.ServiceLayoutBinding
import com.example.gigeconomy.provider.jobDetails
import com.example.gigeconomy.user.ServiceBooked
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
class ServiceLayoutActivity : AppCompatActivity() {

    private lateinit var binding: ServiceLayoutBinding
    private val db = FirebaseFirestore.getInstance()
    private val currentUser = FirebaseAuth.getInstance().currentUser

    private var job: jobDetails? = null   // 🔥 class-level variable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ServiceLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val jobId = intent.getStringExtra("jobId")
        val providerId = intent.getStringExtra("providerId")

        if (jobId.isNullOrEmpty() || providerId.isNullOrEmpty()) {
            Toast.makeText(this, "Invalid job data", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Load job details
        loadJobDetails(jobId, providerId)

        // Book service
        binding.bookService.setOnClickListener {

            if (job == null) {
                Toast.makeText(this, "Job not loaded yet", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (currentUser == null) {
                Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val bookingId = db.collection("bookings").document().id

            val booking = ServiceBooked(
                bookingId = bookingId,
                jobId = job!!.jobId,
                providerId = job!!.providerId,
                userId = currentUser.uid,
                serviceType = job!!.serviceType,
                category = job!!.category,
                rate = job!!.rate,
                city = job!!.city,
                status = "requested",
                bookingTime = System.currentTimeMillis()
            )

            db.collection("bookings")
                .document(bookingId)
                .set(booking)
                .addOnSuccessListener {
                    Toast.makeText(this, "Service booked!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Booking failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
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

                    binding.serviceDisCategory.text = it.category
                    binding.serviceDisType.text = it.serviceType
                    binding.serviceDisAddress.text = it.city
                    binding.serviceDisDescription.text = it.serviceDes
                    binding.serviceDisOwnerName.text = it.ownerName
                    binding.serviceDisRate.text = "₹${it.rate}"
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load job!", Toast.LENGTH_SHORT).show()
            }
    }
}
