package com.example.careerhub.utils
import android.util.Patterns
import java.util.regex.Pattern
import java.util.regex.Matcher
import java.util.regex.PatternSyntaxException

        fun String.isValidEmail(): Boolean {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
        }
        // validate password
        fun String.isValidPassword(): Boolean {
            val passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$"
            return this.matches(passwordRegex.toRegex())
        }
        // validate phone number
        fun String.isValidPhoneNumber(): Boolean {
            val phoneRegex = "^(\\+\\d{1,3}[- ]?)?\\d{10}$"
            return this.matches(phoneRegex.toRegex())
        }
        // validate name
        fun String.isValidName(): Boolean {
            val nameRegex = "^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$"
            return this.matches(nameRegex.toRegex())
        }
        // validate date
        fun String.isValidDate(): Boolean {
            val dateRegex = "^(0[1-9]|1[0-2])/(0[1-9]|1[0-9]|2[0-9]|3[0-1])/\\d{4}\$"
            return this.matches(dateRegex.toRegex())
        }





