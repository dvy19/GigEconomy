package com.example.gigeconomy

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gigeconomy.provider.jobDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ServiceLayoutActivity : AppCompatActivity() {

    private lateinit var serviceDisCategory: TextView
    private lateinit var serviceDisType: TextView
    private lateinit var serviceDisAddress: TextView
    private lateinit var serviceDisDescription: TextView
    private lateinit var serviceDisOwnerName: TextView
    private lateinit var serviceDisRate: TextView

    private val db = FirebaseFirestore.getInstance()
    private val currentUser = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.service_layout)  // your layout XML

        // Find views
        serviceDisCategory = findViewById(R.id.serviceDisCategory)
        serviceDisType = findViewById(R.id.serviceDisType)
        serviceDisAddress = findViewById(R.id.serviceDisAddress)
        serviceDisDescription = findViewById(R.id.serviceDisDescription)
        serviceDisOwnerName = findViewById(R.id.serviceDisOwnerName)
        serviceDisRate = findViewById(R.id.serviceDisRate)

        val jobId = intent.getStringExtra("jobId")

        if (jobId == null || currentUser == null) {
            Toast.makeText(this, "Error loading job!", Toast.LENGTH_SHORT).show()
            return
        }

        loadJobDetails(jobId)
    }

    private fun loadJobDetails(jobId: String) {

        db.collection("providers")
            .document(currentUser!!.uid)
            .collection("jobs")
            .document(jobId)
            .get()
            .addOnSuccessListener { document ->

                if (document.exists()) {
                    val job = document.toObject(jobDetails::class.java)

                    if (job != null) {

                        serviceDisCategory.text = job.category
                        serviceDisType.text = job.serviceType
                        serviceDisAddress.text = job.city
                        serviceDisDescription.text = job.serviceDes
                        serviceDisOwnerName.text = job.ownerName
                        serviceDisRate.text = "₹${job.rate}"

                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load job!", Toast.LENGTH_SHORT).show()
            }
    }
}
