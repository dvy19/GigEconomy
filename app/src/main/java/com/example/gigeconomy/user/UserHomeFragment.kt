package com.example.gigeconomy.user

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.gigeconomy.JobAdapter
import com.example.gigeconomy.databinding.FragmentHomeBinding
import com.example.gigeconomy.provider.jobDetails
import com.example.gigeconomy.user.posts.PostAdapter
import com.example.gigeconomy.user.posts.postCommunity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class UserHomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    // Jobs
    private lateinit var jobAdapter: JobAdapter
    private val jobList = mutableListOf<jobDetails>()

    // Community posts
    private lateinit var postAdapter: PostAdapter
    private val postList = mutableListOf<postCommunity>()

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val uid = auth.currentUser?.uid ?: return


        // -------------------------
        // Load user info (username)
        // -------------------------
        db.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val name = snapshot.getString("fullName") ?: ""
                    binding.homeUsername.text = name
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Unable to load profile", Toast.LENGTH_SHORT)
                    .show()
            }

        // -------------------------
        // Horizontal service types
        // -------------------------
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

        // -------------------------
        // Jobs RecyclerView (vertical)
        // -------------------------
        jobAdapter = JobAdapter(jobList)
        binding.recyclerJobs.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerJobs.adapter = jobAdapter

        loadAllJobs()

        binding.toAllJobs.setOnClickListener {
            startActivity(Intent(requireContext(), AllServicesActivity::class.java))
        }

        // -------------------------
        // Community Posts RecyclerView (horizontal)
        // -------------------------
        postAdapter = PostAdapter(postList)
        binding.rvCommunityPosts.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvCommunityPosts.adapter = postAdapter
        PagerSnapHelper().attachToRecyclerView(binding.rvCommunityPosts)

        loadCommunityPosts()
    }

    // -------------------------
    // Load Community Posts
    // -------------------------
    private fun loadCommunityPosts() {
        db.collection("community_posts")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(10)
            .get()
            .addOnSuccessListener { snapshot ->
                postList.clear()
                for (doc in snapshot.documents) {
                    val post = doc.toObject(postCommunity::class.java)
                    if (post != null) {
                        postList.add(post)
                    }
                }
                postAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            }
    }

    // -------------------------
    // Load all provider jobs + inject providerId
    // -------------------------
    private fun loadAllJobs() {
        jobList.clear()

        db.collection("providers")
            .get()
            .addOnSuccessListener { providerSnapshot ->
                for (providerDoc in providerSnapshot.documents) {
                    val providerId = providerDoc.id

                    db.collection("providers")
                        .document(providerId)
                        .collection("jobs")
                        .get()
                        .addOnSuccessListener { jobSnapshot ->
                            for (jobDoc in jobSnapshot.documents) {
                                val job = jobDoc.toObject(jobDetails::class.java)
                                if (job != null) {
                                    val fixedJob = job.copy(providerId = providerId)
                                    jobList.add(fixedJob)
                                }
                            }
                            jobAdapter.notifyDataSetChanged()
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                requireContext(),
                                "Error loading jobs for provider $providerId",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Unable to load providers", Toast.LENGTH_SHORT)
                    .show()
            }
    }
}
