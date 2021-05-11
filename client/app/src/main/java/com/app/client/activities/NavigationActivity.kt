package com.app.client.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.app.client.adapters.ViewPagerAdapter
import com.app.client.repository.Repository
import com.app.client.databinding.ActivityNavigationBinding
import com.app.client.model.Auction
import java.util.*

class NavigationActivity : FragmentActivity() {

    private val repository = Repository()

    private lateinit var binding: ActivityNavigationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.viewPager.adapter = ViewPagerAdapter(this, ::auctionClick)

        initializeRepository()
    }

    private fun initializeRepository() {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        val userIdString = sharedPref.getString("user-id", null)

        if (userIdString == null)
            repository.initializeWithUserRegistration(this::userIdObtained)
        else
            repository.initialize(UUID.fromString(userIdString))
    }

    private fun userIdObtained(userId: UUID) {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)

        with (sharedPref.edit()) {
            putString("user-id", userId.toString())
            apply()
        }
    }

    private fun auctionClick(auction: Auction){
        val intent = Intent(this, AuctionActivity::class.java)
        intent.putExtra(Auction::id.name, auction.id)
        startActivity(intent)
    }
}