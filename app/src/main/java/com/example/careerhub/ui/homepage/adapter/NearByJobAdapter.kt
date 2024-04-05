package  com.example.careerhub.ui.homepage.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.careerhub.R
import com.example.careerhub.databinding.AdapterNewjobBinding
import com.example.careerhub.model.Jobinformation

class NearByJobAdapter(
    private val onItemClicked: (Jobinformation) -> Unit
) : RecyclerView.Adapter<NearByJobAdapter.NearByJobViewHolder>() {
    private var listJobInformation: List<Jobinformation> = emptyList()

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): NearByJobViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_newjob, parent, false)
        return NearByJobViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NearByJobViewHolder, position: Int) {
        val jobInformation = listJobInformation[position]
        holder.bind(jobInformation)
        holder.itemView.setOnClickListener{
            onItemClicked(jobInformation)
        }
    }

    override fun getItemCount(): Int {
        return listJobInformation.size
    }

    inner class NearByJobViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding: AdapterNewjobBinding =
            AdapterNewjobBinding.bind(itemView)

        fun bind(jobInformation: Jobinformation) {
            binding.txtLeadproduct.text = jobInformation.job_name
            binding.txtFirstscreen.text=jobInformation.companyName
            binding.txtUnitedarab.text = jobInformation.job_starttime + " to " + jobInformation.job_endtime
            binding.txtPrice.text = jobInformation.job_location + " Salary:  " + jobInformation.job_minsalary.toString() +"-"+ jobInformation.job_maxsalary.toString()
        }
    }

    fun submitList(newList: List<Jobinformation>) {
        this.listJobInformation = newList
        notifyDataSetChanged()
    }
}