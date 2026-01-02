package com.example.gigeconomy.user


import android.content.Intent
import android.view.LayoutInflater
import com.example.gigeconomy.R

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
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

        holder.itemView.setOnClickListener {
            val intent = Intent(
                holder.itemView.context,
                CategoryWiseActivity::class.java
            )
            intent.putExtra("CATEGORY", serviceName)
            holder.itemView.context.startActivity(intent)
        }
        // Set correct image based on service name
        val imageRes = when (serviceName) {
            "Chef" ->R.drawable.chef_c
            "Home Cleaning" -> R.drawable.home_c
            "Gardening" -> R.drawable.garden_c
            "Vehicle Repair" -> R.drawable.mechanic_c
            "Electrician" -> R.drawable.electric
            "Plumber" -> R.drawable.plumber_c
            else -> R.drawable.arrow
        }




        holder.binding.imgService.setImageResource(imageRes)
    }

    override fun getItemCount(): Int = serviceList.size
}
