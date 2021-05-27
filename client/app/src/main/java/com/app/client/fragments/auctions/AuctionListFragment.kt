package com.app.client.fragments.auctions

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.client.R
import com.app.client.activities.AuctionActivity
import com.app.client.adapters.AuctionsAdapter
import com.app.client.model.Auction
import com.app.client.repository.Repository

class AuctionListFragment(private val auctionClickListener: (Auction) -> Unit) : Fragment() {

    private val repository = Repository()
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val createdView = inflater.inflate(R.layout.fragment_auction_list, container, false)
        recyclerView = createdView.findViewById(R.id.auctionsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = AuctionsAdapter(auctionClickListener)
        repository.getActionList(this::auctionsObtained)
        return createdView
    }

    private fun auctionsObtained(auctions: ArrayList<Auction>) {
        (recyclerView.adapter as AuctionsAdapter).dataSet = auctions
        recyclerView.adapter?.notifyDataSetChanged()
    }
}