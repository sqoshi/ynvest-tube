package com.app.ynvest_tube.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.app.ynvest_tube.R
import com.app.ynvest_tube.model.Auction
import com.app.ynvest_tube.model.internal.RelativeDate
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class AuctionsAdapter(
    private val clickListener: (Auction) -> Unit
) : RecyclerView.Adapter<AuctionsAdapter.ViewHolder>() {

    var dataSet: ArrayList<Auction> = ArrayList()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val movieNameTextView: TextView = view.findViewById(R.id.movieNameTextView)
        private val startDateTextView: TextView = view.findViewById(R.id.startDateTextView)
        val layout = view.findViewById<ConstraintLayout>(R.id.layout)

        fun bindAuction(auction: Auction) {
            movieNameTextView.text = auction.video.title

            val expirationDateStr = auction.auction_expiration_date
            val relativeDate = RelativeDate(expirationDateStr)
            startDateTextView.text = relativeDate.reprRelativeToNow
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
