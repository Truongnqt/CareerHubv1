package com.example.careerhub.ui.Account

import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.careerhub.R
import com.example.careerhub.databinding.FragmentSignInBinding
import com.example.careerhub.viewmodel.SignInViewModel
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.example.careerhub.utils.SessionManager

class SignIn : Fragment() {
    private lateinit var binding: FragmentSignInBinding
    private lateinit var signInViewModel: SignInViewModel
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var oneTapSignInLauncher: ActivityResultLauncher<IntentSenderRequest>
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        val context = requireContext()
        sessionManager = SessionManager(context)
        //check login


        signInViewModel = ViewModelProvider(this)[SignInViewModel::class.java]


        binding.btnSignIn.setOnClickListener {
            Log.d("InputFields", "${binding.etEmail.text} ${binding.etPassword.text}")
            if (checkEmpty()) {
                signInViewModel.SignIn(
                    binding.etEmail.text.toString(), binding.etPassword.text.toString()
                )
            } else {
                Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
            signInViewModel.signIn.observe(viewLifecycleOwner) {
                Log.d("SignIn", it.toString())
                when (it.status) {
                    200 -> {
                        Toast.makeText(context, "Sign in successfully", Toast.LENGTH_SHORT).show()
                        navigateToSecondActivity()

                        sessionManager.saveCredentials(binding.etEmail.text.toString(), binding.etPassword.text.toString())

                    }

                    401 -> {
                        Toast.makeText(context, "Invalid password", Toast.LENGTH_SHORT).show()
                    }

                    404 -> {
                        Toast.makeText(context, "User not found", Toast.LENGTH_SHORT).show()
                    }

                    else -> {
                        Toast.makeText(context, "Sign in failed", Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }




        binding.etPassword.setOnTouchListener(View.OnTouchListener { _, event ->
            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= binding.etPassword.right - binding.etPassword.compoundDrawables[DRAWABLE_RIGHT].bounds.width()) {
                    togglePasswordVisibility()
                    return@OnTouchListener true
                }
            }
            false
        })

        binding.txtForgotpassword.setOnClickListener {
            val email = binding.etEmail.text.toString()
            signInViewModel.forgotPassword(email)
            signInViewModel.forgotPassword.observe(viewLifecycleOwner) {
                if (it == "Success") {
                    Toast.makeText(context, "Reset password email sent", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Sign in with Google
//        oneTapClient = Identity.getSignInClient(requireActivity())
//        signInRequest = BeginSignInRequest.builder().setPasswordRequestOptions(
//            BeginSignInRequest.PasswordRequestOptions.builder().setSupported(true).build()
//        ).setGoogleIdTokenRequestOptions(
//            BeginSignInRequest.GoogleIdTokenRequestOptions.builder().setSupported(true)
//                .setServerClientId(getString(R.string.client_id))
//                .setFilterByAuthorizedAccounts(false).build()
//        ).setAutoSelectEnabled(true).build()

//         oneTapSignInLauncher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
//            if (result.resultCode == AppCompatActivity.RESULT_OK) {
//                try {
//                    val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
//                    val idToken = credential.googleIdToken
//                    val username = credential.id
//                    val password = credential.password
//
//                    // Use the obtained credentials
//                    Log.d("SignIn", "Authentication done. Username: $username")
//
//                    if (idToken != null) {
//                        Log.d("SignIn", "Got ID token.")
//                    } else if (password != null) {
//                        Log.d("SignIn", "Got password.")
//                    }
//                } catch (e: ApiException) {
//                    Log.e("SignIn", "Google sign-in failed: ${e.message}")
//                }
//            }
//        }

        binding.btnGoogle.setOnClickListener {
            buttonGoogleSignIn()
        }

        return binding.root
    }

    private fun togglePasswordVisibility() {
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
        return binding.etEmail.text.isNotEmpty() && binding.etPassword.text.isNotEmpty()
    }

    private fun navigateToSecondActivity() {
        val navController = NavHostFragment.findNavController(this)
        navController.navigate(R.id.homePageFragment)
    }
    // Sign in with Google

    private fun buttonGoogleSignIn() {
//        oneTapClient.beginSignIn(signInRequest)
//            .addOnSuccessListener(requireActivity()) { result ->
//                try {
//                    val intentSenderRequest = IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
//                    oneTapSignInLauncher.launch(intentSenderRequest)
//                } catch (e: IntentSender.SendIntentException) {
//                    Log.e("SignIn", "Couldn't start One Tap UI: ${e.localizedMessage}")
//                }
//            }
//            .addOnFailureListener(requireActivity()) { e ->
//                Log.e("SignIn", "Google sign-in failed: ${e.localizedMessage}")
//            }
    }
}
