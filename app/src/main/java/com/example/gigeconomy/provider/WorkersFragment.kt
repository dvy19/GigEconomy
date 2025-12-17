package com.example.gigeconomy.provider

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.gigeconomy.R
import com.example.gigeconomy.databinding.FragmentWorkersBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class WorkersFragment : Fragment() {

    private lateinit var binding: FragmentWorkersBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentWorkersBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth= FirebaseAuth.getInstance()
        db= FirebaseFirestore.getInstance()



        binding.btnWorkerSubmit.setOnClickListener{

            val currentUser=auth.currentUser


            if(currentUser==null){
                Toast.makeText(requireContext(),"User not Logged In",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val full_name=binding.workerName.text.toString().trim()
            val exp=binding.workerExp.text.toString()
            val city=binding.City.text.toString()
            val contact=binding.workerContact.text.toString()
            val company=binding.CompanyName.text.toString()
            val category=binding.workerCategory.text.toString()
            val owner=binding.OwnerName.text.toString()

            val workerId=db.collection("providers")
                .document(currentUser.uid)
                .collection("workers")
                .document()
                .id

            val worker= workerDetails(
                name = full_name,
                experience = exp,
                city = city,
                ownerName = owner,
                category = category,
                contact = contact,
                companyName = company
            )

            db.collection("providers")
                .document(currentUser.uid)
                .collection("workers")
                .document(workerId)
                .set(worker)
                .addOnSuccessListener{
                    Toast.makeText(requireContext(),"saved",Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener{e->
                    Toast.makeText(requireContext(),"${e.message}",Toast.LENGTH_SHORT).show()

                }





        }


    }

}