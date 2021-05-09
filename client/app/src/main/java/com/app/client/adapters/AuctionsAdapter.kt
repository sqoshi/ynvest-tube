package com.app.client.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.client.R
import com.app.client.model.Auction

class AuctionsAdapter(
    private val dataSet: ArrayList<Auction>,
    private val clickListener: (Auction) -> Unit
) : RecyclerView.Adapter<AuctionsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val movieNameTextView: TextView = view.findViewById(R.id.movieNameTextView)
        val layout: LinearLayout = view.findViewById(R.id.layout)

        fun bindAuction(auction: Auction) {

            movieNameTextView.text = auction.movieName
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.auction_view, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        val taskItem = dataSet[position]

        viewHolder.bindAuction(taskItem)

        viewHolder.layout.setOnClickListener { clickListener(taskItem) }
    }

    override fun getItemCount() = dataSet.size
}
