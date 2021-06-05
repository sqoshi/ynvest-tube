package com.app.ynvest_tube.activities

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.ynvest_tube.R
import com.app.ynvest_tube.model.Auction
import com.app.ynvest_tube.model.AuctionDetailsResponse
import com.app.ynvest_tube.model.User
import com.app.ynvest_tube.model.internal.Duration
import com.app.ynvest_tube.model.internal.RelativeDate
import com.app.ynvest_tube.refresher.DataRefresher
import com.app.ynvest_tube.repository.Repository
import kotlinx.coroutines.*

class AuctionActivity : AppCompatActivity() {

    private val repository = Repository()
    private val dataRefresher = DataRefresher()
    private var auctionId: Int = 0
    private var auctionExpiration: RelativeDate? = null
    private lateinit var auctionExpirationUpdater: Job
    private var auctionExpirationTextView: TextView? = null
    private var currentUserCash = 0
    private var currentBid = 0
    private var isUserOwnBid = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auction)

        DataRefresher.toastContext = this

        auctionId = intent.getIntExtra(Auction::id.name, 0)

        if (auctionId == 0)
            finish()

        auctionExpirationTextView = findViewById(R.id.auctionActivity_auctionExpiration)
        dataRefresher.subscribeToAuctionEndpoint("viewedAuction_$auctionId", ::auctionDetailsObtained, auctionId)
        dataRefresher.subscribeToUserEndpoint("userCashInAuction", ::userCashUpdated)
    }

    override fun onBackPressed() {
        dataRefresher.unsubscribeToAuctionEndpoint("viewedAuction_$auctionId")
        dataRefresher.unsubscribeToUserEndpoint("userCashInAuction")
        super.onBackPressed()
    }

    override fun onResume() {
        super.onResume()
        dataRefresher.subscribeToAuctionEndpoint("viewedAuction_$auctionId", ::auctionDetailsObtained, auctionId)
        dataRefresher.subscribeToUserEndpoint("userCashInAuction", ::userCashUpdated)
        auctionExpirationUpdater = GlobalScope.launch {
            var scheduledUpdateTime = System.nanoTime()
            scheduledUpdateTime -= scheduledUpdateTime % 1_000_000_000
            scheduledUpdateTime += 1_000_000
            while (true) {
                if (auctionExpiration != null) {
                    if (auctionExpiration!!.secondsLeft <= 0) {
                        finish()
                    }
                }

                runOnUiThread {
                    auctionExpirationTextView?.text = auctionExpiration?.timeLeft
                }
                while (scheduledUpdateTime < System.nanoTime()) {
                    scheduledUpdateTime += 1_000_000_000
                }
                val sleepTime = scheduledUpdateTime - System.nanoTime()
                if (sleepTime > 0) {
                    delay(sleepTime / 1_000_000)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        dataRefresher.unsubscribeToAuctionEndpoint("viewedAuction_$auctionId")
        dataRefresher.unsubscribeToUserEndpoint("userCashInAuction")
        GlobalScope.launch {
            auctionExpirationUpdater.cancelAndJoin()
        }
    }

    private fun auctionDetailsObtained(auctionDetails: AuctionDetailsResponse) {
        insertAuctionData(auctionDetails)
        currentBid = auctionDetails.auction.last_bid_value ?: auctionDetails.auction.starting_price
        isUserOwnBid = auctionDetails.auction.user_contribution == 2
    }

    private fun bidSuccessful(auctionDetails: AuctionDetailsResponse) {
        Toast.makeText(this, "You successfully bet", Toast.LENGTH_SHORT).show()
        repository.getActionDetails(::auctionDetailsObtained, ::requestFailed, auctionId)
    }

    private fun badBidValue() {
        Toast.makeText(this, "Invalid bid value", Toast.LENGTH_SHORT).show()
        repository.getActionDetails(::auctionDetailsObtained, ::requestFailed, auctionId)
    }

    private fun auctionEnded() {
        dataRefresher.unsubscribeToAuctionEndpoint("viewedAuction_$auctionId")
        dataRefresher.unsubscribeToUserEndpoint("userCashInAuction")
        Toast.makeText(this, "Auction has ended", Toast.LENGTH_SHORT).show()
        finish()
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
                auctionDetails.auctionBiddersCount,
                auctionDetails.auctionBiddersCount
            )
        findViewById<TextView>(R.id.auctionActivity_lastBid).text =
            (auctionDetails.auction.last_bid_value
                ?: auctionDetails.auction.starting_price).toString()
        findViewById<TextView>(R.id.auctionActivity_rentalDuration).text =
            Duration(auctionDetails.auction.rental_duration).toString()
        findViewById<TextView>(R.id.auctionActivity_videoViews).text =
            auctionDetails.auction.video.views.toString()
        findViewById<TextView>(R.id.auctionActivity_videoLikes).text =
            auctionDetails.auction.video.likes.toString()

        val expirationDateStr = auctionDetails.auction.auction_expiration_date
        auctionExpiration = RelativeDate(expirationDateStr)
        auctionExpirationTextView?.text = auctionExpiration?.timeLeft

        val auctionStatus = findViewById<TextView>(R.id.auctionActivity_auctionStatus)
        when (auctionDetails.auction.user_contribution) {
            0 -> {
                auctionStatus.visibility = View.GONE
            }
            1 -> {
                auctionStatus.visibility = View.VISIBLE
                auctionStatus.text = resources.getString(R.string.not_highest_bid)
                auctionStatus.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.icon_warning,
                    0,
                    0,
                    0
                )
            }
            2 -> {
                auctionStatus.visibility = View.VISIBLE
                auctionStatus.text = resources.getString(R.string.highest_bid)
                auctionStatus.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.icon_check,
                    0,
                    0,
                    0
                )
            }
        }
    }

    fun onBid(view: View) {
        val bidAmountStr = findViewById<EditText>(R.id.auctionActivity_bidAmount).text.toString()
        if (bidAmountStr.isEmpty()) {
            Toast.makeText(this, "Enter amount", Toast.LENGTH_SHORT).show()
            return
        }

        val bidAmount = bidAmountStr.toInt()
        var cashToBid = currentUserCash
        if(isUserOwnBid)
            cashToBid += currentBid
        
        if(bidAmount > cashToBid) {
            Toast.makeText(this, "You can't afford to bid this high", Toast.LENGTH_SHORT).show()
            return
        }

        if(bidAmount <= currentBid) {
            Toast.makeText(this, "Bid too low", Toast.LENGTH_SHORT).show()
            return
        }

        repository.bidOnAuction(
            ::bidSuccessful, ::requestFailed, ::badBidValue, ::auctionEnded,
            auctionId, bidAmount
        )
    }

    private fun userCashUpdated(user: User){
        currentUserCash = user.cash
    }
}