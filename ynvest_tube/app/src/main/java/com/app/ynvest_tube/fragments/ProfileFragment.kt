package com.app.ynvest_tube.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.ynvest_tube.R
import com.app.ynvest_tube.adapters.AuctionsAdapter
import com.app.ynvest_tube.adapters.CurrentRentalsAdapter
import com.app.ynvest_tube.adapters.PreviousRentalsAdapter
import com.app.ynvest_tube.model.Auction
import com.app.ynvest_tube.model.User
import com.app.ynvest_tube.model.UserDetailsResponse
import com.app.ynvest_tube.repository.Repository

class ProfileFragment : Fragment() {

    private val repository = Repository()
    private lateinit var currentRentals: RecyclerView
    private lateinit var previousRentals: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        currentRentals = view.findViewById(R.id.profileFragment_currentRentalsRecycler)
        currentRentals.layoutManager = LinearLayoutManager(activity)
        currentRentals.adapter = CurrentRentalsAdapter()
        previousRentals = view.findViewById(R.id.profileFragment_previousRentalsRecycler)
        previousRentals.layoutManager = LinearLayoutManager(activity)
        previousRentals.adapter = PreviousRentalsAdapter()
        repository.getUserDetails(::userDetailsObtained, ::requestFailed)
        return view
    }

    private fun userDetailsObtained(userDetails: UserDetailsResponse) {
        (currentRentals.adapter as CurrentRentalsAdapter).dataSet = userDetails.actualRents
        currentRentals.adapter?.notifyDataSetChanged()

        (previousRentals.adapter as PreviousRentalsAdapter).dataSet = userDetails.expiredRents
        previousRentals.adapter?.notifyDataSetChanged()
    }

    private fun requestFailed() {
        Toast.makeText(activity, "Internet connection is not stable", Toast.LENGTH_SHORT).show()
    }
}