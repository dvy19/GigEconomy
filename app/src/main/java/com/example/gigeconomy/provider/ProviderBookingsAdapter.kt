package com.example.gigeconomy.provider

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.gigeconomy.R
import com.example.gigeconomy.ServiceLayoutActivity
import com.example.gigeconomy.user.ServiceBooked
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//this is the adapter of the requested booking item, which gets shows in provider's home screen.
class ProviderBookingsAdapter(
    private val serviceList: List<ServiceBooked>,
    private val onItemClick: (ServiceBooked) -> Unit
) : RecyclerView.Adapter<ProviderBookingsAdapter.ServiceViewHolder>() {

    inner class ServiceViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val tvServiceType: TextView = itemView.findViewById(R.id.tvServiceType)
        val tvCity: TextView = itemView.findViewById(R.id.tvCity)
        val tvRate: TextView = itemView.findViewById(R.id.tvRate)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        val tvBookingTime: TextView = itemView.findViewById(R.id.tvBookingTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_service_booked_provider, parent, false)
        return ServiceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val service = serviceList[position]

        holder.tvServiceType.text =
            "${service.serviceType} • ${service.category}"

        holder.tvCity.text = service.city
        holder.tvRate.text = "₹${service.rate}"
        holder.tvStatus.text = service.status.uppercase()

        holder.tvBookingTime.text =
            "Booked on: ${formatTime(service.bookingTime)}"



        when (service.status) {
            "requested" ->
                holder.tvStatus.setBackgroundResource(R.drawable.bg_status_requested)
            "ongoing" ->
                holder.tvStatus.setBackgroundResource(R.drawable.bg_status_ongoing)
            "completed" ->
                holder.tvStatus.setBackgroundResource(R.drawable.bg_status_completed)
        }
        holder.tvStatus.setOnClickListener {
            onItemClick(service)
        }


    }

    override fun getItemCount(): Int = serviceList.size

    private fun formatTime(timeMillis: Long): String {
        val sdf = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
        return sdf.format(Date(timeMillis))
    }
}

