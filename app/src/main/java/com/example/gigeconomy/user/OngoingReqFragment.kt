package com.example.gigeconomy.user

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

// TODO: Rename parameter arguments, choose names that match

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

    private fun loadOngoingServices(){


        db.collection("bookings")
            .whereEqualTo("userId",currentUser!!.uid)
            .whereIn("status",listOf("requested","ongoing"))
            .get()
            .addOnSuccessListener{snapshot->
                bookingList.clear()
                for(doc in snapshot.documents){
                    val booking=doc.toObject(ServiceBooked::class.java)
                    if(booking!=null) bookingList.add(booking)
                }

                if(bookingList.isEmpty()){
                    Toast.makeText(requireContext(),"no ongoing request",Toast.LENGTH_SHORT).show()
                }

                adapter.notifyDataSetChanged()
            }

            .addOnFailureListener{e->
                Toast.makeText(requireContext(),"${e.message}",Toast.LENGTH_SHORT).show()

            }

    }




}