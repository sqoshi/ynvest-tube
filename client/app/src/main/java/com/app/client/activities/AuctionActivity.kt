package com.app.client.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.app.client.R
import com.app.client.model.Auction
import com.app.client.model.AuctionDetailsResponse
import com.app.client.repository.Repository

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