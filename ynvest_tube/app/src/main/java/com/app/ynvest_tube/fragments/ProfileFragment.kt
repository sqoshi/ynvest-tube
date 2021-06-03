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
import com.app.ynvest_tube.model.UserDetailsResponse
import com.app.ynvest_tube.repository.Repository

class ProfileFragment : Fragment() {

    private val repository = Repository()
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
        repository.getUserDetails(::userDetailsObtained, ::requestFailed)
        return view
    }

    private fun userDetailsObtained(userDetails: UserDetailsResponse) {
        if (userDetails.actualRents.size > 0) {
            (currentRentals.adapter as CurrentRentalsAdapter).dataSet = userDetails.actualRents
            currentRentals.adapter?.notifyDataSetChanged()
            currentRentals.visibility = View.VISIBLE
            emptyCurrentRentals.visibility = View.GONE
        }

        if (userDetails.expiredRents.size > 0) {
            (previousRentals.adapter as PreviousRentalsAdapter).dataSet = userDetails.expiredRents
            previousRentals.adapter?.notifyDataSetChanged()
            previousRentals.visibility = View.VISIBLE
            emptyPreviousRentals.visibility = View.GONE
        }
    }

    private fun requestFailed() {
        Toast.makeText(activity, "Internet connection is not stable", Toast.LENGTH_SHORT).show()
    }
}