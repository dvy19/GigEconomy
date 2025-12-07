package com.example.gigeconomy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gigeconomy.provider.jobDetails
import kotlinx.coroutines.Job

class JobAdapter(private val jobList: List<jobDetails>) :
    RecyclerView.Adapter<JobAdapter.JobViewHolder>() {

    class JobViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val serviceType: TextView = itemView.findViewById(R.id.txtServiceType)
        val companyName: TextView = itemView.findViewById(R.id.txtCompanyName)
        val serviceName: TextView = itemView.findViewById(R.id.txtServiceName)
        val rate: TextView = itemView.findViewById(R.id.txtRate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_job, parent, false)
        return JobViewHolder(view)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val job = jobList[position]
        holder.serviceType.text = job.serviceType
        holder.companyName.text = job.companyName
        holder.rate.text = "₹${job.rate}"
    }

    override fun getItemCount(): Int = jobList.size
}
