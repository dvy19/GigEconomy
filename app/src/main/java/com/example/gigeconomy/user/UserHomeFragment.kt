package com.example.gigeconomy.user

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gigeconomy.JobAdapter
import com.example.gigeconomy.databinding.FragmentHomeBinding
import com.example.gigeconomy.provider.jobDetails
import com.google.firebase.firestore.FirebaseFirestore

class UserHomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var jobAdapter: JobAdapter

    private val jobList = mutableListOf<jobDetails>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // -------------------------------
        // Horizontal Service Types
        // -------------------------------
        val serviceTypes = listOf(
            "Chef",
            "Home Cleaning",
            "Gardening",
            "Vehicle Repair",
            "Electrician",
            "Plumber"
        )

        binding.rvServiceTypes.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvServiceTypes.adapter = ServiceAdapter(serviceTypes)

        // -------------------------------
        // Jobs RecyclerView
        // -------------------------------
        jobAdapter = JobAdapter(jobList)
        binding.recyclerJobs.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerJobs.adapter = jobAdapter

        loadAllJobs()

        binding.toAllJobs.setOnClickListener {
            startActivity(Intent(requireContext(), AllServicesActivity::class.java))
        }
    }

    // ----------------------------------------------------
    // Fetch all provider jobs + inject providerId
    // ----------------------------------------------------
    private fun loadAllJobs() {

        jobList.clear()

        db.collection("providers")
            .get()
            .addOnSuccessListener { providerSnapshot ->

                for (providerDoc in providerSnapshot.documents) {

                    val providerId = providerDoc.id   // 🔥 IMPORTANT

                    db.collection("providers")
                        .document(providerId)
                        .collection("jobs")
                        .get()
                        .addOnSuccessListener { jobSnapshot ->

                            for (jobDoc in jobSnapshot.documents) {

                                val job = jobDoc.toObject(jobDetails::class.java)

                                if (job != null) {
                                    // 🔥 Inject providerId manually
                                    val fixedJob = job.copy(providerId = providerId)
                                    jobList.add(fixedJob)
                                }
                            }

                            jobAdapter.notifyDataSetChanged()
                        }
                }
            }
    }
}
