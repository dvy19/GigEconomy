package com.example.gigeconomy.user


import android.view.LayoutInflater
import com.example.gigeconomy.R

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gigeconomy.databinding.ItemServiceBinding


class ServiceAdapter(
    private val serviceList: List<String>
) : RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder>() {

    inner class ServiceViewHolder(val binding: ItemServiceBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val binding = ItemServiceBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ServiceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val serviceName = serviceList[position]

        holder.binding.txtServiceName.text = serviceName




        // Set correct image based on service name
        val imageRes = when (serviceName) {
            "Chef" ->R.drawable.chef
            "Home Cleaning" -> R.drawable.housekeeping
            "Gardening" -> R.drawable.gardening
            "Vehicle Repair" -> R.drawable.mechanic
            "Electrician" -> R.drawable.electrician
            "Plumber" -> R.drawable.technician
            else -> R.drawable.technician
        }




        holder.binding.imgService.setImageResource(imageRes)
    }

    override fun getItemCount(): Int = serviceList.size
}
