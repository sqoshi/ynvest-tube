package com.app.ynvest_tube.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.ynvest_tube.R
import com.app.ynvest_tube.model.Rent

class PreviousRentalsAdapter : RecyclerView.Adapter<PreviousRentalsAdapter.ViewHolder>() {

    var dataSet: ArrayList<Rent> = ArrayList()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val videoNameTextView = view.findViewById<TextView>(R.id.previousRentalView_videoTitle)
        private val gainsTextView = view.findViewById<TextView>(R.id.previousRentalView_gain)

        fun bindRent(rent: Rent) {
            videoNameTextView.text = rent.auction.video.title
            gainsTextView.text = rent.profit.toString()
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.previous_rental_view, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val taskItem = dataSet[position]
        viewHolder.bindRent(taskItem)
    }

    override fun getItemCount() = dataSet.size
}