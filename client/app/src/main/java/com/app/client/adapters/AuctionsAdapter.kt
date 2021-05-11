package com.app.client.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.recyclerview.widget.RecyclerView
import com.app.client.R
import com.app.client.activities.AuctionActivity
import com.app.client.model.Auction
import java.time.LocalDateTime
import java.time.LocalTime

class AuctionsAdapter(
    private val dataSet: ArrayList<Auction>,
    private val clickListener: (Auction) -> Unit
) : RecyclerView.Adapter<AuctionsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val movieNameTextView: TextView = view.findViewById(R.id.movieNameTextView)
        private val startDateTextView: TextView = view.findViewById(R.id.startDateTextView)
        val layout: LinearLayout = view.findViewById(R.id.layout)

        fun bindAuction(auction: Auction) {
            movieNameTextView.text = auction.movieName

            if (LocalDateTime.now().isBefore(auction.startDate)) {
                layout.setBackgroundColor(Color.GRAY)
                startDateTextView.text = auction.startDate.toString()
            }
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
