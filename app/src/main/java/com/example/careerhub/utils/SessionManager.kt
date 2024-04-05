package com.example.careerhub.utils

import android.content.Context
import android.content.SharedPreferences


class SessionManager(context: Context) {
    private val sharedPreferences: SharedPreferences
    private val editor: SharedPreferences.Editor

    init {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    fun saveCredentials(username: String?, password: String?) {
        editor.putString(KEY_USERNAME, username)
        editor.putString(KEY_PASSWORD, password)
        editor.apply()
    }

    val username: String?
        get() = sharedPreferences.getString(KEY_USERNAME, "")

    val password: String?
        get() = sharedPreferences.getString(KEY_PASSWORD, "")

    val isLoggedIn: Boolean
        get() =// Check if username is available in shared preferences
            sharedPreferences.contains(KEY_USERNAME)

    fun clearSession() {
        editor.clear()
        editor.apply()
    }

    companion object {
        private const val PREF_NAME = "CareerHubPref"
        private const val KEY_USERNAME = "username"
        private const val KEY_PASSWORD = "password"
    }
}