package com.example.gigeconomy.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gigeconomy.JobAdapter
import com.example.gigeconomy.databinding.FragmentHomeBinding
import com.example.gigeconomy.provider.jobDetails
import com.google.firebase.firestore.FirebaseFirestore

class UserHomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val jobList = mutableListOf<jobDetails>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // -------------------------------
        // Setup Horizontal Service Types
        // -------------------------------
        val serviceTypes = listOf(
            "Chef",
            "Home Cleaning",
            "Gardening",
            "Vehicle Repair",
            "Electrician",
            "Plumber"
        )

        val serviceAdapter = ServiceAdapter(serviceTypes)

        binding.rvServiceTypes.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.rvServiceTypes.adapter = serviceAdapter


        // -------------------------------
        // Setup Vertical Jobs Recycler
        // -------------------------------
        binding.recyclerJobs.layoutManager = LinearLayoutManager(requireContext())

        loadAllJobs()

    }


    // ----------------------------------------------------
    // Fetch all provider jobs from Firestore
    // ----------------------------------------------------
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
