package com.app.ynvest_tube.adapters

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
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
    private val clickListener: (Auction) -> Unit,
    private val resources: Resources
) : RecyclerView.Adapter<AuctionsAdapter.ViewHolder>() {

    var dataSet: ArrayList<Auction> = ArrayList()

    class ViewHolder(view: View, private val resources: Resources) : RecyclerView.ViewHolder(view) {
        private val movieNameTextView: TextView = view.findViewById(R.id.movieNameTextView)
        private val startDateTextView: TextView = view.findViewById(R.id.startDateTextView)
        private val auctionStatusImageView: TextView =
            view.findViewById(R.id.auctionView_auctionStatus)
        val layout = view.findViewById<LinearLayout>(R.id.layout)

        fun bindAuction(auction: Auction) {
            movieNameTextView.text = auction.video.title

            val expirationDateStr = auction.auction_expiration_date
            val relativeDate = RelativeDate(expirationDateStr)
            startDateTextView.text = relativeDate.reprRelativeToNow

            when (auction.user_contribution) {
                0 -> {
                    auctionStatusImageView.visibility = View.GONE
                }
                1 -> {
                    auctionStatusImageView.visibility = View.VISIBLE
                    auctionStatusImageView.text = resources.getString(R.string.not_highest_bid)
                    auctionStatusImageView.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.icon_warning,
                        0,
                        0,
                        0
                    )
                }
                2 -> {
                    auctionStatusImageView.visibility = View.VISIBLE
                    auctionStatusImageView.text = resources.getString(R.string.highest_bid)
                    auctionStatusImageView.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.icon_check,
                        0,
                        0,
                        0
                    )
                }
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.auction_view, viewGroup, false)

        return ViewHolder(view, resources)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        val taskItem = dataSet[position]

        viewHolder.bindAuction(taskItem)

        viewHolder.layout.setOnClickListener { clickListener(taskItem) }
    }

    override fun getItemCount() = dataSet.size
}
