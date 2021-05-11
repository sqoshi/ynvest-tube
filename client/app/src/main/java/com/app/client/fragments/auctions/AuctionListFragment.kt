package com.app.client.fragments.auctions

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.client.R
import com.app.client.activities.AuctionActivity
import com.app.client.adapters.AuctionsAdapter
import com.app.client.model.Auction
import com.app.client.repository.Repository

class AuctionListFragment(private val auctionClickListener: (Auction) -> Unit) : Fragment() {

    private val repository = Repository()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        repository.getActionList(this::auctionsObtained)
        return inflater.inflate(R.layout.fragment_auction_list, container, false)
    }

    private fun auctionsObtained(auctions: ArrayList<Auction>){
        view!!.findViewById<RecyclerView>(R.id.auctionsRecyclerView).adapter = AuctionsAdapter(auctions, auctionClickListener)
    }
}