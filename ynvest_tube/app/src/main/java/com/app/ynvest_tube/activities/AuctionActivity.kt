package com.app.ynvest_tube.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.app.ynvest_tube.R
import com.app.ynvest_tube.model.Auction
import com.app.ynvest_tube.model.AuctionDetailsResponse
import com.app.ynvest_tube.repository.Repository

class AuctionActivity : AppCompatActivity() {

    private val repository = Repository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auction)

        val auctionId = intent.getIntExtra(Auction::id.name, 0)

        if(auctionId == 0)
            finish()

        repository.getActionDetails(::auctionDetailsObtained, ::requestFailed, auctionId)
    }

    private fun auctionDetailsObtained(auctionDetails: AuctionDetailsResponse){
        //Example bid call
        repository.bidOnAuction(::bidSuccessful, ::requestFailed, ::notEnoughValueInBid, ::auctionEnded,
                auctionDetails.auction.id, (auctionDetails.auction.last_bid_value ?: auctionDetails.auction.starting_price) + 1)
        throw NotImplementedError()
    }

    private fun bidSuccessful(auctionDetails: AuctionDetailsResponse){
        throw NotImplementedError()
    }

    private fun notEnoughValueInBid(){
        throw NotImplementedError()
    }

    private fun auctionEnded(){
        throw NotImplementedError()
    }

    private fun requestFailed(){
        Toast.makeText(this, "Internet connection is not stable", Toast.LENGTH_SHORT).show()
    }
}