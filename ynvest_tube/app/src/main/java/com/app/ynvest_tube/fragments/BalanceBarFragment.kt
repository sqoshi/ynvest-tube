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
import com.app.ynvest_tube.repository.Repository

class BalanceBarFragment : Fragment() {

    private val repository = Repository()
    private lateinit var fragmentView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentView = inflater.inflate(R.layout.fragment_balance_bar, container, false)
        repository.getUser(::userObtained, ::requestFailed)
        return fragmentView
    }

    private fun userObtained(user: User) {
        fragmentView.findViewById<TextView>(R.id.balanceBarFragment_userBalance)?.text =
            user.cash.toString()
    }

    private fun requestFailed() {
        Toast.makeText(activity, "Internet connection is not stable", Toast.LENGTH_SHORT).show()
    }
}