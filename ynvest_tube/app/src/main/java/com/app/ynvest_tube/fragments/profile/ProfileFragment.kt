package com.app.ynvest_tube.fragments.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.app.ynvest_tube.R
import com.app.ynvest_tube.adapters.AuctionsAdapter
import com.app.ynvest_tube.model.Auction
import com.app.ynvest_tube.model.User
import com.app.ynvest_tube.model.UserDetailsResponse
import com.app.ynvest_tube.repository.Repository

class ProfileFragment : Fragment() {

    private val repository = Repository()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        repository.getUser(::userObtained, ::requestFailed)
        repository.getUserDetails(::userDetailsObtained, ::requestFailed)
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    private fun userObtained(user: User) {
        throw NotImplementedError()
    }

    private fun userDetailsObtained(userDetails: UserDetailsResponse) {
        throw NotImplementedError()
    }

    private fun requestFailed(){
        Toast.makeText(activity, "Internet connection is not stable", Toast.LENGTH_SHORT).show()
    }
}