package com.app.ynvest_tube.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.app.ynvest_tube.R
import com.app.ynvest_tube.model.Auction
import com.app.ynvest_tube.model.AuctionDetailsResponse
import com.app.ynvest_tube.model.internal.RelativeDate
import com.app.ynvest_tube.repository.Repository

class AuctionActivity : AppCompatActivity() {

    private val repository = Repository()
    private var auctionId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auction)

        auctionId = intent.getIntExtra(Auction::id.name, 0)

        if (auctionId == 0)
            finish()

        repository.getActionDetails(::auctionDetailsObtained, ::requestFailed, auctionId)
    }

    private fun auctionDetailsObtained(auctionDetails: AuctionDetailsResponse) {
        insertAuctionData(auctionDetails)
    }

    private fun bidSuccessful(auctionDetails: AuctionDetailsResponse) {
        Toast.makeText(this, "You successfully bet", Toast.LENGTH_SHORT).show()
        insertAuctionData(auctionDetails)
    }

    private fun notEnoughValueInBid() {
        Toast.makeText(this, "Bid too low", Toast.LENGTH_SHORT).show()

        // todo: make sure to call refresh so the current highest bid is updated
    }

    private fun auctionEnded() {
        Toast.makeText(this, "Auction has ended", Toast.LENGTH_SHORT).show()
        finish()

        // todo: make sure to call refresh so the auction does not appear on the list
    }

    private fun requestFailed() {
        Toast.makeText(this, "Internet connection is not stable", Toast.LENGTH_SHORT).show()
    }

    private fun insertAuctionData(auctionDetails: AuctionDetailsResponse) {
        findViewById<TextView>(R.id.auctionActivity_videoTitle).text =
            auctionDetails.auction.video.title
        findViewById<TextView>(R.id.auctionActivity_bidders).text =
            getString(R.string.bidders_amount, auctionDetails.auctionBidders)
        findViewById<TextView>(R.id.auctionActivity_lastBid).text =
            (auctionDetails.auction.last_bid_value ?: auctionDetails.auction.starting_price).toString()
        findViewById<TextView>(R.id.auctionActivity_rentalDuration).text =
            auctionDetails.auction.rental_duration

        val expirationDateStr = auctionDetails.auction.auction_expiration_date
        val relativeDate = RelativeDate(expirationDateStr)
        findViewById<TextView>(R.id.auctionActivity_auctionExpiration).text = relativeDate.timeLeft
    }

    fun onBid(view: View) {
        val bidAmountStr = findViewById<EditText>(R.id.auctionActivity_bidAmount).text.toString()
        if (bidAmountStr.isEmpty()) {
            Toast.makeText(this, "Enter amount", Toast.LENGTH_SHORT).show()
            return
        }
        val bidAmount = bidAmountStr.toInt()
        repository.bidOnAuction(
                ::bidSuccessful, ::requestFailed, ::notEnoughValueInBid, ::auctionEnded,
                auctionId, bidAmount
        )
    }
}