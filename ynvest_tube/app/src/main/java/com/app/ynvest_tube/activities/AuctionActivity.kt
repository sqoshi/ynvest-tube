package com.app.ynvest_tube.activities

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.ynvest_tube.R
import com.app.ynvest_tube.fragments.BalanceBarFragment
import com.app.ynvest_tube.model.Auction
import com.app.ynvest_tube.model.AuctionDetailsResponse
import com.app.ynvest_tube.model.internal.Duration
import com.app.ynvest_tube.model.internal.RelativeDate
import com.app.ynvest_tube.refresher.AuctionSubscriber
import com.app.ynvest_tube.refresher.DataRefresher
import com.app.ynvest_tube.refresher.UserDetailsSubscriber
import com.app.ynvest_tube.repository.Repository
import kotlinx.coroutines.*

class AuctionActivity : AppCompatActivity() {

    private val repository = Repository()
    private var auctionId: Int = 0
    private var auctionExpiration: RelativeDate? = null
    private lateinit var auctionExpirationUpdater: Job
    private var auctionExpirationTextView: TextView? = null

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auction)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        DataRefresher.toastContext = this

        auctionId = intent.getIntExtra(Auction::id.name, 0)

        if (auctionId == 0)
            finish()

        auctionExpirationTextView = findViewById(R.id.auctionActivity_auctionExpiration)
        DataRefresher.auctionSubscribers["viewedAuction_$auctionId"] = AuctionSubscriber(::insertAuctionData, auctionId)
    }

    override fun onBackPressed() {
        DataRefresher.auctionSubscribers.remove("viewedAuction_$auctionId")
        super.onBackPressed()
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
        repository.getActionDetails(::auctionDetailsObtained, ::requestFailed, auctionId)
    }

    private fun notEnoughValueInBid() {
        Toast.makeText(this, "Bid too low", Toast.LENGTH_SHORT).show()
        repository.getActionDetails(::auctionDetailsObtained, ::requestFailed, auctionId)
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
        repository.bidOnAuction(
            ::bidSuccessful, ::requestFailed, ::notEnoughValueInBid, ::auctionEnded,
            auctionId, bidAmount
        )
    }
}