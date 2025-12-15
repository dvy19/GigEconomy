package com.example.gigeconomy.user

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gigeconomy.R
import com.example.gigeconomy.databinding.OngoingServiceLayoutBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import okhttp3.internal.notify

class OngoingServicesActivity : AppCompatActivity() {

    private lateinit var binding: OngoingServiceLayoutBinding
    private  var db= FirebaseFirestore.getInstance()
    private var currentUser=FirebaseAuth.getInstance().currentUser
    private lateinit var adapter: OngoingServicesAdapter
    private var bookingList=mutableListOf<ServiceBooked>()
    override fun onCreate(savedInstanceState: Bundle?) {

        binding= OngoingServiceLayoutBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if(currentUser==null){
            Toast.makeText(this,"USer not logged in",Toast.LENGTH_SHORT).show()
            return
        }

        setupRecyclerView()

        loadOngoingServices()

    }

    private fun setupRecyclerView(){
        adapter= OngoingServicesAdapter(bookingList)
        binding.ongoingServices.layoutManager= LinearLayoutManager(this)
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
                    Toast.makeText(this,"no ongoing request",Toast.LENGTH_SHORT).show()
                }

                adapter.notifyDataSetChanged()
            }

            .addOnFailureListener{e->
                Toast.makeText(this,"${e.message}",Toast.LENGTH_SHORT).show()

            }
        
    }
}