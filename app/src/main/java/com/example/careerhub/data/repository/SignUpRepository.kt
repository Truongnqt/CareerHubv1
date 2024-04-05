package com.example.careerhub.data.repository

import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.careerhub.data.remote.SignUpApi
import com.example.careerhub.model.SignUpRespone
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Response

class SignUpRepository {
    val retrofitInstance = Retrofit.Builder().baseUrl("http://139.59.242.16/")
        .addConverterFactory(GsonConverterFactory.create()).build().create(SignUpApi::class.java)

    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    suspend fun signUp(
        email: String, password: String, name: String, phone: String
    ): Response<SignUpRespone> {
        return retrofitInstance.signUp(email, password, name, phone)
    }

    suspend fun registerUser(enail: String, password: String,  result: (String) -> Unit) {
        // register user
        auth.createUserWithEmailAndPassword(enail, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    Log.d("Register", "createUserWithEmail:success")
                    result("Success")
                } else {
                    // If sign in fails, display a message to the user.
                }
            }
            .addOnFailureListener {
                // Handle Errors here
                Log.d("Register", "createUserWithEmail:failure", it)
                result("${it.message.toString()}")
            }
    }
}