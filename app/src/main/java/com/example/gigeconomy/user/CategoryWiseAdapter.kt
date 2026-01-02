package com.example.gigeconomy.user

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gigeconomy.R
import com.example.gigeconomy.provider.jobDetails

class CategoryWiseAdapter(
    private val jobs: List<jobDetails>
) : RecyclerView.Adapter<CategoryWiseAdapter.JobViewHolder>() {

    inner class JobViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val serviceType: TextView = itemView.findViewById(R.id.ServiceType)
        val companyName: TextView = itemView.findViewById(R.id.companyName)
        val rate: TextView = itemView.findViewById(R.id.rate)
        //val imgService: ImageView = itemView.findViewById(R.id.imgCategory)
        val city:TextView=itemView.findViewById(R.id.serviceCity)

        val viewJobDetail: Button= itemView.findViewById(R.id.viewJobDetail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_job, parent, false)
        return JobViewHolder(view)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val job = jobs[position]

        holder.serviceType.text = job.serviceType
        holder.companyName.text = job.companyName
        holder.rate.text = "₹${job.rate}"
        holder.city.text=job.city
    }

    override fun getItemCount() = jobs.size
}
