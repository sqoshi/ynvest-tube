package com.app.client.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.app.client.R
import com.app.client.Repository
import java.util.*

class ProfileActivity : AppCompatActivity() {

    private val repository = Repository()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        initializeRepository()
    }

    private fun initializeRepository() {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        var userId = UUID.fromString(sharedPref.getString("user-id", null))

        if (userId == null) {
            userId = repository.initializeWithUserRegistration()
        } else {
            repository.initialize(userId)
        }

        with (sharedPref.edit()) {
            putString("user-id", userId.toString())
            apply()
        }
    }
}