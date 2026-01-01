package com.example.gigeconomy

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.gigeconomy.provider.jobDetails

//shown in home screen of  user

class JobAdapter(private val jobList: List<jobDetails>) :
    RecyclerView.Adapter<JobAdapter.JobViewHolder>() {

    class JobViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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

        // 🔥 Button click inside RecyclerView item


        val job = jobList[position]

        holder.serviceType.text = job.serviceType
        holder.companyName.text = job.companyName
        holder.rate.text = "₹${job.rate}"
        holder.city.text=job.city
        //holder.imgService.setImageResource(getCategoryImage(job.category))



        holder.viewJobDetail.setOnClickListener {
            val context = holder.itemView.context

            Log.d("JOB_ADAPTER", "jobId=${job.jobId}, providerId=${job.providerId}")

            if (job.jobId.isEmpty()) {
                Toast.makeText(context, "Invalid job id", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (job.providerId.isEmpty()) {
                Toast.makeText(context, "Invalid provider id", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            val intent = Intent(context, ServiceLayoutActivity::class.java)
            intent.putExtra("jobId", job.jobId)
            intent.putExtra("providerId", job.providerId)

            context.startActivity(intent)
        }
        //holder.imgService.setImageResource(getCategoryImage(job.category))
    }



    override fun getItemCount(): Int = jobList.size
}
