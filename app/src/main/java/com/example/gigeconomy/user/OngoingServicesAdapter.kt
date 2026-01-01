package com.example.gigeconomy.user

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.gigeconomy.R
import com.example.gigeconomy.ServiceLayoutActivity
import com.example.gigeconomy.provider.jobDetails

class OngoingServicesAdapter(private val bookingList: List<ServiceBooked>) :
    RecyclerView.Adapter<OngoingServicesAdapter.BookingViewHolder>() {



    class BookingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val serviceType: TextView = itemView.findViewById(R.id.txtServiceType)
        val companyName: TextView = itemView.findViewById(R.id.txtCompanyName)
        val rate: TextView = itemView.findViewById(R.id.txtRate)
        val status: TextView = itemView.findViewById(R.id.ServiceStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.ongoing_service_item, parent, false)
        return BookingViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {


        val booking = bookingList[position]

        holder.serviceType.text = booking.serviceType
        holder.companyName.text = booking.category
        holder.rate.text = "₹${booking.rate}"
       // holder.imageCategory.setImageResource(getCategoryImage(booking.category))
       holder.status.text=booking.status


        when (booking.status) {
            "confirmed" -> holder.status.text = "Ongoing"
            "requested" -> holder.status.text = "Requested"
            "completed" -> holder.status.text = "Completed"
            else -> holder.status.text = booking.status
        }



    }


    override fun getItemCount(): Int = bookingList.size
}
