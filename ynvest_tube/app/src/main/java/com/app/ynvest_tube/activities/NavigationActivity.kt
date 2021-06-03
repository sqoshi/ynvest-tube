package com.app.ynvest_tube.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.app.ynvest_tube.adapters.ViewPagerAdapter
import com.app.ynvest_tube.repository.Repository
import com.app.ynvest_tube.databinding.ActivityNavigationBinding
import com.app.ynvest_tube.model.Auction
import java.util.*

class NavigationActivity : FragmentActivity() {

    private val repository = Repository()
    private val viewPagerAdapter: ViewPagerAdapter = ViewPagerAdapter(this, ::auctionClick)

    private lateinit var binding: ActivityNavigationBinding

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        binding.viewPager.adapter = viewPagerAdapter

        initializeRepository()
    }

    private fun initializeRepository() {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        val userIdString = sharedPref.getString("user-id", null)

        if (userIdString == null)
            repository.initializeWithUserRegistration(::userIdObtained, ::requestFailed)
        else {
            repository.initialize(UUID.fromString(userIdString))
            viewPagerAdapter.activate()
        }
    }

    private fun userIdObtained(userId: UUID) {
        viewPagerAdapter.activate()
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

    private fun requestFailed(){
        Toast.makeText(this, "Internet connection is not stable", Toast.LENGTH_SHORT).show()
        repository.initializeWithUserRegistration(::userIdObtained, ::requestFailed)
    }
}