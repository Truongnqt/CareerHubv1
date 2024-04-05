package com.example.careerhub.ui.homepage.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.careerhub.R
import com.example.careerhub.databinding.AdapterJobInformationBinding
import com.example.careerhub.model.Jobinformation

class JobInformationAdapter(
    private val onItemClicked: (Jobinformation) -> Unit
) : RecyclerView.Adapter<JobInformationAdapter.JobInformationViewHolder>(){
    private var listJobInformation: List<Jobinformation> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobInformationViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_job_information, parent, false)
        return JobInformationViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: JobInformationViewHolder, position: Int) {
        val jobInformation = listJobInformation[position]
        holder.bind(jobInformation)
        holder.itemView.setOnClickListener {
            onItemClicked(jobInformation)
        }
    }

    override fun getItemCount(): Int {
        return listJobInformation.size
    }

    inner class JobInformationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding: AdapterJobInformationBinding = AdapterJobInformationBinding.bind(itemView)

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val jobInformation = listJobInformation[position]

                }
            }
        }

        fun bind(jobInformation: Jobinformation) {
            binding.txtProductName.text = jobInformation.job_name
            binding.txtLocation.text = jobInformation.job_location
        }
    }

    fun submitList(newList: List<Jobinformation>) {
        this.listJobInformation = newList
        notifyDataSetChanged()
    }
}