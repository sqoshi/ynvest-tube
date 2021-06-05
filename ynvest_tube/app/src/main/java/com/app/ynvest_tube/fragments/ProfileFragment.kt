package com.app.ynvest_tube.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.ynvest_tube.R
import com.app.ynvest_tube.adapters.CurrentRentalsAdapter
import com.app.ynvest_tube.adapters.PreviousRentalsAdapter
import com.app.ynvest_tube.model.Rent
import com.app.ynvest_tube.model.UserDetailsResponse
import com.app.ynvest_tube.refresher.DataRefresher
import com.app.ynvest_tube.refresher.UserDetailsSubscriber
import com.app.ynvest_tube.refresher.UserSubscriber
import com.app.ynvest_tube.repository.Repository

class ProfileFragment : Fragment() {

    private val dataRefresher = DataRefresher()
    private lateinit var currentRentals: RecyclerView
    private lateinit var previousRentals: RecyclerView
    private lateinit var emptyCurrentRentals: TextView
    private lateinit var emptyPreviousRentals: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        emptyCurrentRentals = view.findViewById(R.id.profileFragment_currentRentalsEmpty)
        emptyPreviousRentals = view.findViewById(R.id.profileFragment_previousRentalsEmpty)
        currentRentals = view.findViewById(R.id.profileFragment_currentRentalsRecycler)
        currentRentals.layoutManager = LinearLayoutManager(activity)
        currentRentals.adapter = CurrentRentalsAdapter()
        currentRentals.isNestedScrollingEnabled = false
        previousRentals = view.findViewById(R.id.profileFragment_previousRentalsRecycler)
        previousRentals.layoutManager = LinearLayoutManager(activity)
        previousRentals.adapter = PreviousRentalsAdapter()
        previousRentals.isNestedScrollingEnabled = false
        dataRefresher.subscribeToUserDetailsEndpoint("userDetailsView", ::userDetailsObtained)
        return view
    }

    override fun onStop() {
        dataRefresher.unsubscribeToUserDetailsEndpoint("userDetailsView")
        super.onStop()
    }

    override fun onResume() {
        dataRefresher.subscribeToUserDetailsEndpoint("userDetailsView", ::userDetailsObtained)
        super.onResume()
    }

    private fun userDetailsObtained(
        previousUserDetails: UserDetailsResponse?,
        userDetails: UserDetailsResponse
    ) {
        userDetails.actualRents.sortBy { it.auction.rental_expiration_date }
        (currentRentals.adapter as CurrentRentalsAdapter).dataSet = userDetails.actualRents
        currentRentals.adapter?.notifyDataSetChanged()

        if (userDetails.actualRents.size > 0) {
            currentRentals.visibility = View.VISIBLE
            emptyCurrentRentals.visibility = View.GONE
        } else {
            currentRentals.visibility = View.GONE
            emptyCurrentRentals.visibility = View.VISIBLE
        }

        userDetails.expiredRents.sortByDescending { it.auction.rental_expiration_date }
        (previousRentals.adapter as PreviousRentalsAdapter).dataSet = userDetails.expiredRents
        previousRentals.adapter?.notifyDataSetChanged()

        if (userDetails.expiredRents.size > 0) {
            previousRentals.visibility = View.VISIBLE
            emptyPreviousRentals.visibility = View.GONE
        } else {
            previousRentals.visibility = View.GONE
            emptyPreviousRentals.visibility = View.VISIBLE
        }

        if (previousUserDetails != null) {
            val currentIds = userDetails.actualRents.map { it.id }
            previousUserDetails.actualRents.forEach {
                if (!currentIds.contains(it.id)) {
                    val title =
                        if (it.auction.video.title.length > 15)
                            it.auction.video.title.take(12) + "..."
                        else
                            it.auction.video.title
                    val message = "Rental of $title has ended"
                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}