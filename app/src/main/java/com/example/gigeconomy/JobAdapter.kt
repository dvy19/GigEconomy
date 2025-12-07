package com.example.gigeconomy

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
        val imageCategory: ImageView = itemView.findViewById(R.id.imgCategory)

        // 🔥 ADD THIS
        val viewJobDetail: View = itemView.findViewById(R.id.viewJobDetail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_job, parent, false)
        return JobViewHolder(view)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {

        // 🔥 Button click inside RecyclerView item


        val job = jobList[position]
        holder.serviceType.text = job.serviceType
        holder.companyName.text = job.companyName
        holder.rate.text = "₹${job.rate}"

        holder.viewJobDetail.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ServiceLayoutActivity::class.java)
            intent.putExtra("jobId", job.jobId)
            context.startActivity(intent)
        }
        holder.imageCategory.setImageResource(getCategoryImage(job.category))
    }

    private fun getCategoryImage(category: String): Int {
        return when (category.lowercase()) {
            "plumber" -> R.drawable.technician
            "chef" -> R.drawable.chef
            "electrician" -> R.drawable.electrician
            "vehicle" -> R.drawable.mechanic
            "gardening" -> R.drawable.gardening
            else -> R.drawable.chef
        }
    }

    override fun getItemCount(): Int = jobList.size
}
