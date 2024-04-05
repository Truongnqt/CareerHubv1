package com.example.careerhub.ui.listallJob

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.careerhub.R
import com.example.careerhub.databinding.FragmentListAllJobBinding
import com.example.careerhub.ui.homepage.adapter.JobInformationAdapter
import com.example.careerhub.viewmodel.JobInformationViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.careerhub.ui.homepage.adapter.ListItemJobAdapter






// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ListAllJobFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListAllJobFragment : Fragment() {
    private lateinit var binding: FragmentListAllJobBinding
    private lateinit var jobviewModel: JobInformationViewModel
    private lateinit var listadapter : ListItemJobAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        jobviewModel = ViewModelProvider(this).get(JobInformationViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListAllJobBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        jobviewModel.getInformation()
        jobviewModel.jobInformation.observe(viewLifecycleOwner, Observer {
            listadapter.submitList(it!!.jobinformations)

        })

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
        if (query.isEmpty()) {
            jobviewModel.getInformation()
            jobviewModel.jobInformation.observe(viewLifecycleOwner, Observer {
                listadapter.submitList(it!!.jobinformations)

            })
            return
        }
        else {
            jobviewModel.filterJobInformation(query.trim())
            jobviewModel.jobInformationByFilter.observe(
                viewLifecycleOwner,
                Observer {
                    listadapter.submitList(it!!.jobinformations)
                    Log.d("JobInformation", it.jobinformations.toString())
                }
            )

        }

    }


    private fun setupRecyclerView() {
        listadapter = ListItemJobAdapter(
            onItemClicked = { jobInformation ->
                val action = ListAllJobFragmentDirections.actionLisalljobFragmentToJobDetailFragment(jobInformation.id)
                findNavController().navigate(action)
            }
        )

        binding.recyclerUserprofile.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = listadapter
        }

    }


}