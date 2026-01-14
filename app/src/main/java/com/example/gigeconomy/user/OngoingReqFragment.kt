package com.example.gigeconomy.user

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gigeconomy.R
import com.example.gigeconomy.databinding.OngoingServiceLayoutBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class OngoingReqFragment : Fragment() {

    private lateinit var binding: OngoingServiceLayoutBinding
    private  var db= FirebaseFirestore.getInstance()
    private var currentUser=FirebaseAuth.getInstance().currentUser
    private lateinit var adapter: OngoingServicesAdapter
    private var bookingList=mutableListOf<ServiceBooked>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding= OngoingServiceLayoutBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(currentUser==null){
            Toast.makeText(requireContext(),"USer not logged in",Toast.LENGTH_SHORT).show()
            return
        }

        setupRecyclerView()

        loadOngoingServices()
    }

    private fun setupRecyclerView(){
        adapter= OngoingServicesAdapter(bookingList)
        binding.ongoingServices.layoutManager= LinearLayoutManager(requireContext())
        binding.ongoingServices.adapter=adapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadOngoingServices(){


            db.collection("bookings")
                .whereEqualTo("userId", currentUser!!.uid)
                .whereIn("status", listOf("requested", "confirmed", "ongoing"))
                .addSnapshotListener { snapshot, error ->

                    if (error != null) {
                        Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
                        return@addSnapshotListener
                    }

                    bookingList.clear()



                    snapshot?.documents?.forEach { doc ->
                        val booking = doc.toObject(ServiceBooked::class.java)
                        if (booking != null && booking.userId == currentUser!!.uid) {
                            bookingList.add(booking)
                        }
                    }

                    if (bookingList.isEmpty()) {
                        Toast.makeText(requireContext(), "No ongoing request", Toast.LENGTH_SHORT).show()
                    }

                    adapter.notifyDataSetChanged()
                }
        }


    }


