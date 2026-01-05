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

        // Image picker
        imagePickerLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                if (uri != null) {
                    binding.profileImage.setImageURI(uri)
                    uploadImageToFirebase(uri)
                }
            }

        binding.addUserpic.setOnClickListener {
            imagePickerLauncher.launch("image/*")
        }

        binding.userLogout.setOnClickListener {
            auth.signOut()
            val intent = Intent(requireContext(), GetStarted::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }

        loadProfile()
    }


    private fun uploadImageToFirebase(imageUri: Uri) {
        val uid = auth.currentUser?.uid ?: return

        val storageRef = storage.reference
            .child("profile_images/$uid/profile.jpg")


        storageRef.putFile(imageUri)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    saveImageUrlToFirestore(downloadUrl.toString())
                }
            }
            .addOnFailureListener {e->
                Toast.makeText(requireContext(), "Upload failed : ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // 🔹 Save image URL to Firestore
    private fun saveImageUrlToFirestore(imageUrl: String) {
        val uid = auth.currentUser?.uid ?: return

        db.collection("users")
            .document(uid)
            .update("profileImageUrl", imageUrl)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Profile photo updated", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to save image", Toast.LENGTH_SHORT).show()
            }
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
