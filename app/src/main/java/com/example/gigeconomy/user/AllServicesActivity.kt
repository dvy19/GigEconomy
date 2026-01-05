package com.example.gigeconomy.user

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gigeconomy.JobAdapter
import com.example.gigeconomy.databinding.ViewServicesBinding
import com.example.gigeconomy.provider.jobDetails
import com.google.firebase.firestore.FirebaseFirestore


//this is the screen where all services are listed with a filter.
// when user clicks the view all in home fragment then it moves to here.
class AllServicesActivity : AppCompatActivity() {

    private lateinit var binding: ViewServicesBinding
    private lateinit var db: FirebaseFirestore
    private val jobList = mutableListOf<jobDetails>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ViewServicesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()

        val categoryTypes = listOf(
            "All",
            "Home",
            "Electrician",
            "Gardening",
            "Vehicle Repair",
            "Plumber"
        )

        // Setup horizontal category RecyclerView with click listener
        val categoryAdapter = CategoryAdapter(categoryTypes) { selectedCategory ->
            filterJobs(selectedCategory)
        }

        binding.rvCategoryTypes.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvCategoryTypes.adapter = categoryAdapter

        // Setup vertical jobs RecyclerView
        binding.recyclerJobs.layoutManager = LinearLayoutManager(this)

        loadAllJobs()
    }

    private fun filterJobs(category: String) {
        val filteredJobs = if (category == "All") {
            jobList
        } else {
            jobList.filter { it.category == category }
        }

        // Update your RecyclerView adapter
        binding.recyclerJobs.adapter = JobAdapter(filteredJobs)
    }

    private fun loadAllJobs() {
        db.collection("providers")
            .get()
            .addOnSuccessListener { providerSnapshot ->

                for (providerDoc in providerSnapshot) {
                    db.collection("providers")
                        .document(providerDoc.id)
                        .collection("jobs")
                        .get()
                        .addOnSuccessListener { jobSnapshot ->

                            for (jobDoc in jobSnapshot) {
                                val job = jobDoc.toObject(jobDetails::class.java)
                                jobList.add(job)
                            }

                            // Set adapter after adding jobs
                            binding.recyclerJobs.adapter = JobAdapter(jobList)
                        }
                }
            }
    }
}
