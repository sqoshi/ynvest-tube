package com.app.ynvest_tube.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.app.ynvest_tube.R
import com.app.ynvest_tube.model.User
import com.app.ynvest_tube.refresher.DataRefresher

class BalanceBarFragment : Fragment() {

    private val dataRefresher = DataRefresher()
    private lateinit var fragmentView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentView = inflater.inflate(R.layout.fragment_balance_bar, container, false)
        dataRefresher.subscribeToUserEndpoint("barView_${activity?.componentName}", ::userObtained)
        return fragmentView
    }

    private fun userObtained(user: User) {
        fragmentView.findViewById<TextView>(R.id.balanceBarFragment_userBalance)?.text =
            user.cash.toString()
    }

    override fun onResume() {
        dataRefresher.subscribeToUserEndpoint("barView_${activity?.componentName}", ::userObtained)
        super.onResume()
    }

    override fun onStop() {
        dataRefresher.unsubscribeToUserEndpoint("barView_${activity?.componentName}")
        super.onStop()
    }
}