package com.app.client.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.app.client.R
import com.app.client.Repository
import com.app.client.ViewPagerAdapter
import com.app.client.databinding.ActivityNavigationBinding
import java.util.*

class NavigationActivity : FragmentActivity() {

    private val repository = Repository()

    private lateinit var binding: ActivityNavigationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.viewPager.adapter = ViewPagerAdapter(this)

        initializeRepository()
    }

    private fun initializeRepository() {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        val userIdString = sharedPref.getString("user-id", null)
        val userId : UUID

        if (userIdString == null) {
            userId = repository.initializeWithUserRegistration()
        } else {
            userId = UUID.fromString(userIdString)
            repository.initialize(userId)
        }

        with (sharedPref.edit()) {
            putString("user-id", userId.toString())
            apply()
        }
    }
}