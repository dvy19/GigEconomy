package com.example.gigeconomy.provider


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gigeconomy.R
import com.example.gigeconomy.user.ServiceBooked
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProviderBookingsAdapter
    private val serviceList = mutableListOf<ServiceBooked>()

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private var listener: ListenerRegistration? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_provider_home, container, false)

        recyclerView = view.findViewById(R.id.serviceRequests)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = ProviderBookingsAdapter(serviceList) { service ->
            // TODO: Navigate to Job Details / Accept Screen
        }

        recyclerView.adapter = adapter

        fetchRequestedJobs()

        return view
    }

    private fun fetchRequestedJobs() {

        val providerId = auth.currentUser?.uid ?: return

        listener = db.collection("bookings")
            .whereEqualTo("providerId", providerId)
            .whereEqualTo("status", "requested") // only new jobs
            .addSnapshotListener { snapshot, error ->

                if (error != null || snapshot == null) return@addSnapshotListener

                serviceList.clear()

                for (doc in snapshot.documents) {
                    val booking = doc.toObject(ServiceBooked::class.java)
                    booking?.let { serviceList.add(it) }
                }

                adapter.notifyDataSetChanged()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        listener?.remove() // prevent memory leak
    }
}
