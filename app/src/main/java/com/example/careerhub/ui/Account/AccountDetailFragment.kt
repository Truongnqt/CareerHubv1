package com.example.careerhub.ui.Account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.careerhub.R
import com.example.careerhub.databinding.FragmentAccountDetailBinding
import com.example.careerhub.utils.SessionManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import com.example.careerhub.model.User
import com.example.careerhub.viewmodel.InformationUserViewModel

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AccountDetailFragment : Fragment() {
    private lateinit var binding: FragmentAccountDetailBinding
    private lateinit var inforuserviewModel : InformationUserViewModel
    private val sessionManager: SessionManager by lazy { SessionManager(requireContext()) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inforuserviewModel = ViewModelProvider(this)[InformationUserViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountDetailBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inforuserviewModel.getUser(sessionManager.username.toString())
        inforuserviewModel.user.observe(viewLifecycleOwner, Observer {
            binding.profileName.text = it.jobseeker[0].name
            binding.emailText.text = it.jobseeker[0].email
            binding.phoneNumberText.text = it.jobseeker[0].phone
            binding.locationText.text = it.jobseeker[0].address
            binding.birthdayText.text = it.jobseeker[0].birthday

        })
        binding.iconBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        binding.iconSettings.setOnClickListener {
            val navHostFragment =
                requireActivity().supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
            val navController = navHostFragment.navController
            navController.navigate(R.id.editProfileFragment)
        }
    }


}