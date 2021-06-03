package com.app.ynvest_tube.activities

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.ynvest_tube.R
import com.app.ynvest_tube.model.Auction
import com.app.ynvest_tube.model.AuctionDetailsResponse
import com.app.ynvest_tube.model.internal.Duration
import com.app.ynvest_tube.model.internal.RelativeDate
import com.app.ynvest_tube.repository.Repository
import kotlinx.coroutines.*

class AuctionActivity : AppCompatActivity() {

    private val repository = Repository()
    private var auctionId: Int = 0
    private var auctionExpiration: RelativeDate? = null
    private lateinit var auctionExpirationUpdater: Job
    private var auctionExpirationTextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auction)

        auctionId = intent.getIntExtra(Auction::id.name, 0)

        if (auctionId == 0)
            finish()

        auctionExpirationTextView = findViewById(R.id.auctionActivity_auctionExpiration)
        repository.getActionDetails(::auctionDetailsObtained, ::requestFailed, auctionId)
    }

    override fun onResume() {
        super.onResume()
        auctionExpirationUpdater = GlobalScope.launch {
            var scheduledUpdateTime = System.nanoTime()
            while (true) {
                val startTime = System.nanoTime()
                auctionExpirationTextView?.text = auctionExpiration?.timeLeft
                val sleepTime = 1_000_000_000 + scheduledUpdateTime - startTime
                scheduledUpdateTime += 1_000_000_000
                if (sleepTime > 0) {
                    delay(sleepTime / 1_000_000)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        GlobalScope.launch {
            auctionExpirationUpdater.cancelAndJoin()
        }
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
            resources.getQuantityString(
                R.plurals.bidders_amount,
                auctionDetails.auctionBidders,
                auctionDetails.auctionBidders
            )
        findViewById<TextView>(R.id.auctionActivity_lastBid).text =
            (auctionDetails.auction.last_bid_value ?: auctionDetails.auction.starting_price).toString()
        findViewById<TextView>(R.id.auctionActivity_rentalDuration).text =
            Duration(auctionDetails.auction.rental_duration).toString()
        findViewById<TextView>(R.id.auctionActivity_videoViews).text =
            auctionDetails.auction.video.views.toString()
        findViewById<TextView>(R.id.auctionActivity_videoLikes).text =
            auctionDetails.auction.video.likes.toString()

        val expirationDateStr = auctionDetails.auction.auction_expiration_date
        auctionExpiration = RelativeDate(expirationDateStr)
        auctionExpirationTextView?.text = auctionExpiration?.timeLeft
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