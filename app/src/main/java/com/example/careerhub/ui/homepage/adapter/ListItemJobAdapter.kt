package com.example.careerhub.ui.homepage.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.careerhub.R
import com.example.careerhub.databinding.ItemListjobBinding
import com.example.careerhub.model.Jobinformation
import com.bumptech.glide.Glide

class ListItemJobAdapter(
    private val onItemClicked: (Jobinformation) -> Unit
) : RecyclerView.Adapter<ListItemJobAdapter.ListItemJobViewHolder>() {
    private var listListItemJob: List<Jobinformation> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemJobViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_listjob, parent, false)
        return ListItemJobViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ListItemJobViewHolder, position: Int) {
        val jobInformation = listListItemJob[position]
        holder.bind(jobInformation)
        holder.itemView.setOnClickListener {
            onItemClicked(jobInformation)
        }
    }

    override fun getItemCount(): Int {
        return listListItemJob.size
    }

    fun submitList(newList: List<Jobinformation>) {
        this.listListItemJob = newList
        notifyDataSetChanged()
    }

    inner class ListItemJobViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding: ItemListjobBinding = ItemListjobBinding.bind(itemView)
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val jobInformation = listListItemJob[position]

                }
            }
        }

        fun bind(jobInformation: Jobinformation) {
            binding.jobTitle.text = jobInformation.job_name
            binding.jobInfo.text =
                jobInformation.job_starttime + " to " + jobInformation.job_endtime
            val linkimage = "http://139.59.242.16/" + jobInformation.companyLogo.toString()
            // set image from api is link image from cloud
            Glide.with(itemView.context)
                .load(linkimage)
                .centerCrop().placeholder(R.drawable.img_image_amazonlogo)
                .into(binding.amazonLogo)
                // use place holder


        }

        }


}