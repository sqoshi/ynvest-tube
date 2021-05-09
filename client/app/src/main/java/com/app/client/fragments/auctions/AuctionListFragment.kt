package com.app.client.fragments.auctions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.client.R
import com.app.client.adapters.AuctionsAdapter
import com.app.client.model.Auction
import com.app.client.repository.Repository

class AuctionListFragment : Fragment() {

    private val repository = Repository()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        repository.getActionList(this::auctionsObtained)
        return inflater.inflate(R.layout.fragment_auction_list, container, false)
    }

    private fun auctionsObtained(auctions: ArrayList<Auction>){
        view!!.findViewById<RecyclerView>(R.id.auctionsRecyclerView).adapter = AuctionsAdapter(auctions, this::auctionClick)
    }

    private fun auctionClick(auction: Auction) {
        throw NotImplementedError()
    }
}