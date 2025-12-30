package com.example.gigeconomy.provider

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.gigeconomy.R
import com.example.gigeconomy.databinding.ActivityRequestServiceLayoutBinding
import com.example.gigeconomy.user.ServiceBooked
import com.google.firebase.firestore.FirebaseFirestore

class RequestServiceLayout : AppCompatActivity() {

    private lateinit var binding: ActivityRequestServiceLayoutBinding
    private lateinit var bookingId: String

    private val  db= FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityRequestServiceLayoutBinding.inflate(layoutInflater)

        bookingId = intent.getStringExtra("bookingId") ?: return

        setContentView(binding.root)



    }

    private fun loadBookingAndUser(bookingId: String) {
        val db = FirebaseFirestore.getInstance()

        db.collection("bookings")
            .document(bookingId)
            .get()
            .addOnSuccessListener { bookingDoc ->

                val booking = bookingDoc.toObject(ServiceBooked::class.java)
                    ?: return@addOnSuccessListener

                // ✅ Bind booking/service details
                bindBookingData(booking)

                // ✅ Load EXACT user who requested
                loadRequestingUser(booking.userId)
            }
    }

    private fun loadRequestingUser(userId: String) {
        val db = FirebaseFirestore.getInstance()

        db.collection("users")
            .document(userId)   // 🔥 USER A UID
            .get()
            .addOnSuccessListener { userDoc ->

                if (!userDoc.exists()) return@addOnSuccessListener

                val userName = userDoc.getString("fullName") ?: "N/A"
                val userAdd = userDoc.getString("homeAddress") ?: "N/A"

               binding.requestDisUserName.text=userName
                binding.requestDisUserAddress.text=userAdd
            }
    }


    private fun bindBookingData(booking: ServiceBooked) {
       binding.requestDisType.text=booking.serviceType
        binding.requestDisCategory.text=booking.category
        binding.requestDisRate.text=booking.rate
        binding.requestDisAddress.text=booking.city
    }
}