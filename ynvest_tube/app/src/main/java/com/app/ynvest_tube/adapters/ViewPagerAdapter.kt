package com.app.ynvest_tube.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.app.ynvest_tube.fragments.AuctionListFragment
import com.app.ynvest_tube.fragments.ProfileFragment
import com.app.ynvest_tube.model.Auction

class ViewPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val auctionClickListener: (Auction) -> Unit
) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount() : Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ProfileFragment()
            1 -> AuctionListFragment(auctionClickListener)
            else -> throw IllegalArgumentException()
        }
    }
}