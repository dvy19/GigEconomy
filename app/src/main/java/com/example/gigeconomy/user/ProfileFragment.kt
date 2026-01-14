package com.example.gigeconomy.user

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.gigeconomy.GetStarted
import com.example.gigeconomy.R
import com.example.gigeconomy.databinding.UserFragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class ProfileFragment : Fragment() {

    private lateinit var binding: UserFragmentProfileBinding
    private lateinit var imagePickerLauncher: ActivityResultLauncher<String>

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = UserFragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.appTopBar.tvTitle.text = "Profile"




        binding.userLogout.setOnClickListener {
            auth.signOut()
            val intent = Intent(requireContext(), GetStarted::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }

        loadProfile()
    }




    // 🔹 Load profile data
    private fun loadProfile() {
        val uid = auth.currentUser?.uid ?: return

        db.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {

                    binding.userDisName.text =
                        snapshot.getString("fullName") ?: ""

                    binding.userDisAddress.text =
                        snapshot.getString("homeAddress") ?: ""

                    binding.userDisCity.text =
                        snapshot.getString("City") ?: ""

                    val imageUrl = snapshot.getString("profileImageUrl")

                    if (!imageUrl.isNullOrEmpty()) {
                        Glide.with(requireContext())
                            .load(imageUrl)
                            .placeholder(R.drawable.boy)
                            .into(binding.profileImage)
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Unable to load profile", Toast.LENGTH_SHORT).show()
            }
    }
}
