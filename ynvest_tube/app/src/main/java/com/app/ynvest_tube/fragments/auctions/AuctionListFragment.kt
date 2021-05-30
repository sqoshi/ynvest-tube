package com.app.ynvest_tube.fragments.auctions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.ynvest_tube.R
import com.app.ynvest_tube.adapters.AuctionsAdapter
import com.app.ynvest_tube.model.Auction
import com.app.ynvest_tube.repository.Repository

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
        repository.getActionList(::auctionsObtained, ::requestFailed)
        return createdView
    }

    private fun auctionsObtained(auctions: ArrayList<Auction>) {
        (recyclerView.adapter as AuctionsAdapter).dataSet = auctions
        recyclerView.adapter?.notifyDataSetChanged()
    }

    private fun requestFailed(){
        Toast.makeText(activity, "Internet connection is not stable", Toast.LENGTH_SHORT).show()
    }
}