package com.app.client

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.app.client.fragments.auctions.AuctionListFragment
import com.app.client.fragments.profile.ProfileFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            1 -> ProfileFragment()
            2 -> AuctionListFragment()
            else -> throw IllegalArgumentException()
        }
    }
}