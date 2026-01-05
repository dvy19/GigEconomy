package com.example.gigeconomy.user

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gigeconomy.databinding.CategoryJobsBinding
import com.example.gigeconomy.databinding.ItemJobBinding
import com.example.gigeconomy.provider.jobDetails
import com.google.firebase.firestore.FirebaseFirestore


//this is the activity which shows category wise jobs

class CategoryWiseActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CategoryWiseAdapter
    private val jobList = mutableListOf<jobDetails>()



    private lateinit var binding: CategoryJobsBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        binding= CategoryJobsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val category = intent.getStringExtra("CATEGORY") ?: return

        binding.tvCategoryTitle.text = "$category Jobs"

        recyclerView = binding.rvJobs
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CategoryWiseAdapter(jobList)
        recyclerView.adapter = adapter

        fetchJobsByCategory(category)
    }

    private fun fetchJobsByCategory(category: String) {
        FirebaseFirestore.getInstance()
            .collection("jobs")
            .whereEqualTo("category", category)
            .get()
            .addOnSuccessListener { snapshot ->
                jobList.clear()
                for (doc in snapshot) {
                    jobList.add(doc.toObject(jobDetails::class.java))
                }
                adapter.notifyDataSetChanged()
            }
    }
}
