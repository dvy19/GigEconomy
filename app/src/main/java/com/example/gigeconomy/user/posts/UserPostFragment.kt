package com.example.gigeconomy.user.posts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.gigeconomy.databinding.FragmentUserPostBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserPostFragment : Fragment() {
    private lateinit var binding: FragmentUserPostBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        binding.submitPost.setOnClickListener {

            val currentUser = auth.currentUser ?: return@setOnClickListener

            val company = binding.postCompany.text.toString().trim()
            val content=binding.postContent.text.toString().trim()

            if (company.isEmpty() || content.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            firestore.collection("users")
                .document(currentUser.uid)
                .get()
                .addOnSuccessListener { document ->

                    val username = document.getString("username") ?: "Unknown"

                    val postId = firestore.collection("community_posts").document().id

                    val post = postCommunity(
                        postId = postId,
                        userId = currentUser.uid,
                        username = username,
                        company = company,
                        content = content
                    )

                    firestore.collection("community_posts")
                        .document(postId)
                        .set(post)
                        .addOnSuccessListener {
                            Toast.makeText(requireContext(), "Post published!", Toast.LENGTH_SHORT).show()
                        }
                }

        }


    }}