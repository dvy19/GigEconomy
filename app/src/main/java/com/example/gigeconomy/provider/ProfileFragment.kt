package com.example.gigeconomy.provider

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gigeconomy.GetStarted
import com.example.gigeconomy.R
import com.example.gigeconomy.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private  var auth= FirebaseAuth.getInstance()
    private var db= FirebaseFirestore.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding= FragmentProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.providerLogout.setOnClickListener{
            FirebaseAuth.getInstance().signOut()

            val intent = Intent(requireContext(), GetStarted::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)

            requireActivity().finish()
        }

    }


    private fun providerDetail(){

        val uid=auth.currentUser?.uid?:return

        db.collection("provider")
            .document(uid)
            .get()
            .addOnSuccessListener{ doc->
                if(doc.exists()){
                    val name = doc.getString("fullName") ?: ""
                    val homeAddress = doc.getString("homeAddress") ?: ""
                    val city = doc.getString("City") ?: ""


                    // Set data to UI
                    binding.providerDisName.text = name
                    binding.providerDisAddress.text = homeAddress
                    binding.providerDisCity.text = city
                }
            }

    }
}