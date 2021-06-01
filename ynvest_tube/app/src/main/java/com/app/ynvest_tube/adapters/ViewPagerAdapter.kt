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

    private var activated = false

    override fun getItemCount() : Int {
        if(!activated)
            return 0
        return 2
    }

    public fun activate() {
        activated = true
        notifyDataSetChanged()
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ProfileFragment()
            1 -> AuctionListFragment(auctionClickListener)
            else -> throw IllegalArgumentException()
        }
    }
}