package com.app.ynvest_tube.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.ynvest_tube.R
import com.app.ynvest_tube.model.Rent
import com.app.ynvest_tube.model.internal.RelativeDate

class CurrentRentalsAdapter : RecyclerView.Adapter<CurrentRentalsAdapter.ViewHolder>() {

    var dataSet: ArrayList<Rent> = ArrayList()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val videoNameTextView =
            view.findViewById<TextView>(R.id.currentRentalView_videoTitle)
        private val beginningViewsTextView =
            view.findViewById<TextView>(R.id.currentRentalView_beginningViews)
        private val endingViewsTextView =
            view.findViewById<TextView>(R.id.currentRentalView_endingViews)
        private val moneySpentTextView =
            view.findViewById<TextView>(R.id.currentRentalView_moneySpent)
        private val moneyGainedTextView =
            view.findViewById<TextView>(R.id.currentRentalView_moneyGained)
        private val rentalEndTimeTextView =
            view.findViewById<TextView>(R.id.currentRentalView_rentalEndTime)

        fun bindRent(rent: Rent) {
            videoNameTextView.text = rent.auction.video.title
            beginningViewsTextView.text = rent.auction.video_views_on_sold?.toString()
            endingViewsTextView.text = rent.auction.video.views.toString()
            moneySpentTextView.text = rent.auction.last_bid_value?.toString()
            moneyGainedTextView.text =
                (rent.auction.video.views - rent.auction.video_views_on_sold!!).toString()
            rentalEndTimeTextView.text = RelativeDate(rent.auction.rental_expiration_date).reprRelativeToNow
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.current_rental_view, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val taskItem = dataSet[position]
        viewHolder.bindRent(taskItem)
    }

    override fun getItemCount() = dataSet.size
}