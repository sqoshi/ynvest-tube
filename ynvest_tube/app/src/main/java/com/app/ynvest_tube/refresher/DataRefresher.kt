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

        public var userSubscribers = mutableMapOf<String, UserSubscriber>()
        public var userDetailsSubscribers = mutableMapOf<String, UserDetailsSubscriber>()
        public var auctionListSubscribers = mutableMapOf<String, AuctionListSubscriber>()
        public var auctionSubscribers = mutableMapOf<String, AuctionSubscriber>()

        public var toastContext: Context? = null
    }

    public fun startRefresher() {
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

                sleep(1000)
            }
        })

        refreshTask.start()
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
        if(toastContext != null)
            Toast.makeText(toastContext, "Internet connection is not stable", Toast.LENGTH_SHORT).show()
    }
}