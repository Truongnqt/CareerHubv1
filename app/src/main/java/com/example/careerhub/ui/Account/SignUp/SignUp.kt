package com.example.careerhub.ui.Account.SignUp

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.careerhub.databinding.FragmentSignUpBinding
import androidx.navigation.fragment.NavHostFragment
import com.example.careerhub.R
import com.example.careerhub.viewmodel.SignupViewModel

class SignUp : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    private lateinit var signupViewModel: SignupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    // check sign in status
    private fun navigateToSecondActivity() {
        val navHostFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        val navController = navHostFragment.navController
        navController.navigate(R.id.homePageFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        binding.etPassword.setOnTouchListener(View.OnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= binding.etPassword.right - binding.etPassword.compoundDrawables[DRAWABLE_RIGHT].bounds.width()) {
                    togglePasswordVisibility(binding.etPassword)
                    return@OnTouchListener true
                }
            }
            false
        })
        binding.etcfPassword.setOnTouchListener(View.OnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= binding.etcfPassword.right - binding.etcfPassword.compoundDrawables[DRAWABLE_RIGHT].bounds.width()) {
                    togglePasswordVisibility(binding.etcfPassword)
                    return@OnTouchListener true
                }
            }
            false
        })
        // sign up button
        binding.btnSignUp.setOnClickListener(View.OnClickListener {
            if (checkEmpty()) {
                val email = binding.etEmail.text.toString()
                val password = binding.etPassword.text.toString()
//                val name = binding.etUserName.text.toString()
//                val phone = binding.etMobileNumber.text.toString()

                val viewModel = ViewModelProvider(this).get(SignupViewModel::class.java)
                viewModel.registerUser(email, password)

                viewModel.registerUser.observe(viewLifecycleOwner) {
                    if (it == "Success") {
                        Toast.makeText(context, "Sign up successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    }
                }
//                viewModel.SignUp(email, password, name, phone)
//                viewModel.signUp.observe(viewLifecycleOwner, Observer {
//                    if (it != null) {
//                        if (it.status == 200) {
//                            navigateToSecondActivity()
//                        } else {
//                            Toast.makeText(context, it.messages, Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                })
            } else {
                Toast.makeText(context, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
            }
        })
        binding.txtreturnLogin.setOnClickListener(View.OnClickListener {
            val navController = NavHostFragment.findNavController(this)
            navController.navigate(R.id.signIn)
        })

        // Inflate the layout for this fragment
        return binding.root
    }
    // toggle password visibility

    private fun togglePasswordVisibility(etPassword: EditText) {
        binding.etPassword.inputType =
            if (binding.etPassword.inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            } else {
                InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            }
        val drawable =
            if (binding.etPassword.inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                R.drawable.img_icon_viewpassword
            } else {
                R.drawable.imd_hide
            }
        binding.etPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable, 0)
    }
     private fun checkEmpty(): Boolean {
         return binding.etEmail.text.isNotEmpty()
                 && binding.etPassword.text.isNotEmpty()
//                 && binding.etUserName.text.isNotEmpty()
//                 && binding.etcfPassword.text.isNotEmpty()
//                 && binding.etcfPassword.text.isValidPassword()
//                 && binding.etMobileNumber.text.isValidPhoneNumber()
     }
    private fun CharSequence?.isValidPassword(): Boolean {
        if (this == null) {
            return false
        }
        val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$"
        val passwordMatcher = Regex(passwordPattern)
        return passwordMatcher.find(this) != null
}
    private fun CharSequence?.isValidPhoneNumber(): Boolean {
        if (this == null) {
            return false
        }
        val phonePattern = "^[+]?[0-9]{10,13}$"
        val phoneMatcher = Regex(phonePattern)
        return phoneMatcher.find(this) != null
    }
}