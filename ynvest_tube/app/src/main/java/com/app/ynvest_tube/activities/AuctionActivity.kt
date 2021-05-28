package com.app.ynvest_tube.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        repository.getActionDetails(::auctionDetailsObtained, auctionId)
    }

    private fun auctionDetailsObtained(auctionDetails: AuctionDetailsResponse){
        throw NotImplementedError()
    }
}