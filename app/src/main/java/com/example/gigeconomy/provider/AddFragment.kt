package com.example.gigeconomy.provider

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.gigeconomy.R
import com.example.gigeconomy.databinding.FragmentProviderAddBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class AddFragment : Fragment() {

    private lateinit var binding: FragmentProviderAddBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProviderAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        binding.btnServviceSubmit.setOnClickListener {

            val currentUser = auth.currentUser
            if (currentUser == null) {
                Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val serviceType = binding.ServiceType.text.toString()
            val companyName = binding.CompanyName.text.toString()
            val ownerName = binding.OwnerName.text.toString()
            val rate = binding.Rate.text.toString()
            val serviceDes = binding.Description.text.toString()
            val city = binding.City.text.toString()
            val contact = binding.Contact.text.toString()

            val category=binding.ServiceCategory.text.toString()

            // Optional: Check required fields
            if (serviceType.isEmpty() || ownerName.isEmpty() || rate.isEmpty()) {
                Toast.makeText(requireContext(), "Fill all required fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val jobId = firestore.collection("providers")
                .document(currentUser.uid)
                .collection("jobs")
                .document()
                .id

            val job = jobDetails(
                jobId = jobId,  // add jobId inside object
                providerId = currentUser.uid,
                serviceType = serviceType,
                category = category,
                serviceDes = serviceDes,
                ownerName = ownerName,
                city = city,
                contact = contact,
                rate = rate
            )


            // Save job
            firestore.collection("providers")
                .document(currentUser.uid)
                .collection("jobs")
                .document(jobId)
                .set(job)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Job Saved Successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }

        }
    }

}
