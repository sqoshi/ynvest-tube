package com.app.ynvest_tube.refresher

import com.app.ynvest_tube.model.Auction
import java.util.ArrayList

data class AuctionListSubscriber(val successful: (ArrayList<Auction>) -> Unit)