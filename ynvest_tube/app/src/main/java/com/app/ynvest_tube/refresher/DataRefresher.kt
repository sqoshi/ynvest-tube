package com.app.ynvest_tube.refresher

import android.content.Context
import android.widget.TextView
import android.widget.Toast
import com.app.ynvest_tube.R
import com.app.ynvest_tube.adapters.AuctionsAdapter
import com.app.ynvest_tube.adapters.CurrentRentalsAdapter
import com.app.ynvest_tube.adapters.PreviousRentalsAdapter
import com.app.ynvest_tube.model.Auction
import com.app.ynvest_tube.model.AuctionDetailsResponse
import com.app.ynvest_tube.model.User
import com.app.ynvest_tube.model.UserDetailsResponse
import com.app.ynvest_tube.repository.Repository
import java.lang.Thread.sleep

class DataRefresher {
    companion object {
        private lateinit var repository: Repository

        private lateinit var refreshTask: Thread

        private var userSubscribers = mutableMapOf<String, UserSubscriber>()
        private var userDetailsSubscribers = mutableMapOf<String, UserDetailsSubscriber>()
        private var auctionListSubscribers = mutableMapOf<String, AuctionListSubscriber>()
        private var auctionSubscribers = mutableMapOf<String, AuctionSubscriber>()

        var toastContext: Context? = null
    }

    private val refreshRateInMilis: Long = 2000

    fun startRefresher() {
        repository = Repository()

        refreshTask = Thread(Runnable {
            while (true) {
                if (userSubscribers.any())
                    repository.getUser(::userObtained, ::requestFailed)

                if (userDetailsSubscribers.any())
                    repository.getUserDetails(::userDetailsObtained, ::requestFailed)

                if (auctionListSubscribers.any())
                    repository.getActionList(::auctionListObtained, ::requestFailed)

                for (subscriber in auctionSubscribers.values) {
                    repository.getActionDetails(
                        subscriber.successful,
                        ::requestFailed,
                        subscriber.auctionId
                    )
                }

                sleep(refreshRateInMilis)
            }
        })

        refreshTask.start()
    }

    fun subscribeToUserEndpoint(key: String, successful: (User) -> Unit) {
        userSubscribers[key] = UserSubscriber(successful)

        repository.getUser(::userObtained, ::requestFailed)
    }

    fun subscribeToUserDetailsEndpoint(key: String, successful: (UserDetailsResponse) -> Unit) {
        userDetailsSubscribers[key] = UserDetailsSubscriber(successful)

        repository.getUserDetails(::userDetailsObtained, ::requestFailed)
    }

    fun subscribeToAuctionListEndpoint(key: String, successful: (ArrayList<Auction>) -> Unit) {
        auctionListSubscribers[key] = AuctionListSubscriber(successful)

        repository.getActionList(::auctionListObtained, ::requestFailed)
    }

    fun subscribeToAuctionEndpoint(
        key: String,
        successful: (AuctionDetailsResponse) -> Unit,
        auctionId: Int
    ) {
        auctionSubscribers[key] = AuctionSubscriber(successful, auctionId)

        repository.getActionDetails(
            successful,
            ::requestFailed,
            auctionId
        )
    }

    fun unsubscribeToUserEndpoint(key: String) {
        userSubscribers.remove(key)
    }

    fun unsubscribeToUserDetailsEndpoint(key: String) {
        userDetailsSubscribers.remove(key)
    }

    fun unsubscribeToAuctionListEndpoint(key: String) {
        auctionListSubscribers.remove(key)
    }

    fun unsubscribeToAuctionEndpoint(key: String) {
        auctionSubscribers.remove(key)
    }

    private fun auctionListObtained(auctions: ArrayList<Auction>) {
        for (subscriber in auctionListSubscribers.values) {
            subscriber.successful(auctions)
        }
    }

    private fun userObtained(user: User) {
        for (subscriber in userSubscribers.values) {
            subscriber.successful(user)
        }
    }

    private fun userDetailsObtained(userDetails: UserDetailsResponse) {
        for (subscriber in userDetailsSubscribers.values) {
            subscriber.successful(userDetails)
        }
    }

    private fun requestFailed() {
        if (toastContext != null)
            Toast.makeText(toastContext, "Internet connection is not stable", Toast.LENGTH_SHORT)
                .show()
    }
}