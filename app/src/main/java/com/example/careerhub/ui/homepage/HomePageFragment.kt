package com.example.careerhub.ui.homepage

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.careerhub.R
import com.example.careerhub.databinding.FragmentHomepageBinding
import com.example.careerhub.ui.homepage.adapter.JobInformationAdapter
import com.example.careerhub.ui.homepage.adapter.NearByJobAdapter
import com.example.careerhub.utils.SessionManager
import com.example.careerhub.viewmodel.InformationUserViewModel
import com.example.careerhub.viewmodel.JobInformationViewModel


class HomePageFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var jobInformationViewModel : JobInformationViewModel
    private lateinit var binding: FragmentHomepageBinding
    private lateinit var jobInformationAdapter: JobInformationAdapter
    private lateinit var nearByJobAdapter: NearByJobAdapter
    private lateinit var inforUser: InformationUserViewModel
    private val sessionManager: SessionManager by lazy { SessionManager(requireContext()) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        jobInformationViewModel = ViewModelProvider(this)[JobInformationViewModel::class.java]
        inforUser =ViewModelProvider(this)[InformationUserViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomepageBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        jobInformationViewModel.getInformation()
        jobInformationViewModel.jobInformation.observe(viewLifecycleOwner)
        {
            jobInformationAdapter.submitList(it!!.jobinformations)

        }
        inforUser.getUser(sessionManager.username.toString())
        inforUser.user.observe(viewLifecycleOwner, Observer {
            var linkimage= "http://139.59.242.16/" + it.jobseeker[0].avatar.toString()
            Glide.with(this)
                .load(linkimage)
                .transform(CircleCrop())
                .placeholder(R.drawable.img_illustrations_r)
                .into(binding.imageImageOne)
            binding.txtHisimone.text ="Hi " + it.jobseeker[0].name.toString()
        })

        binding.imageImageOne.setOnClickListener {
            val navController = NavHostFragment.findNavController(this)
            navController.navigate(R.id.accountDetailFragment)
        }
        jobInformationViewModel.jobInformation.observe(viewLifecycleOwner)
        { nearByJobAdapter.submitList(it!!.jobinformations) }
        binding.textInputEditTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                onSearchQueryChanged(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                // Do nothing
            }
    })

    }
    private fun onSearchQueryChanged(query: String) {
        //show results
        if(query.isEmpty()){
            binding.txtNoresult.visibility = View.GONE
            binding.recyclerViewSearchResults.visibility = View.GONE
            return
        }
        jobInformationViewModel.getInformationBySuggest(query.trim())
        if(jobInformationViewModel.jobInformationBySuggest.value != null){
            binding.recyclerViewSearchResults.visibility = View.VISIBLE
            jobInformationViewModel.jobInformationBySuggest.observe(viewLifecycleOwner, Observer {
                nearByJobAdapter.submitList(it!!.jobinformations)
            })
        }
        else
        {
            return
        }

        }



    private fun setupRecyclerView() {
    jobInformationAdapter = JobInformationAdapter(
        onItemClicked = { jobInformation ->
            Log.d("JobInformationAdapter", "onItemClicked: $jobInformation")
            val action = HomePageFragmentDirections.actionHomePageFragmentToJobDetailFragment(jobInformation.id)
            findNavController().navigate(action)
        }
    )

    binding.recyclerUserprofile.apply {
        layoutManager = LinearLayoutManager(requireContext())
        adapter = jobInformationAdapter
    }
    nearByJobAdapter = NearByJobAdapter(
        onItemClicked = {
                jobinformation ->
            val action = HomePageFragmentDirections.actionHomePageFragmentToJobDetailFragment(jobinformation.id)
            findNavController().navigate(action)
        }
    )
    binding.recyclerUserprofile1.apply {
        layoutManager = LinearLayoutManager(requireContext())
        adapter = nearByJobAdapter
    }
    binding.txtShowallOne.setOnClickListener {
        val navController = NavHostFragment.findNavController(this)
        navController.navigate(R.id.listAllJobFragment)
    }
    binding.txtShowall.setOnClickListener {
        val navController = NavHostFragment.findNavController(this)
        navController.navigate(R.id.listAllJobFragment)
    }
    binding.recyclerViewSearchResults.apply {
        layoutManager = LinearLayoutManager(requireContext())
        adapter = nearByJobAdapter
    }
}
}
