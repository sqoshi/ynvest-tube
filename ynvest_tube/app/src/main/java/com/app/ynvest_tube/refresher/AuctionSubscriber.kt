package com.app.ynvest_tube.refresher

import com.app.ynvest_tube.model.AuctionDetailsResponse

data class AuctionSubscriber(val successful: (AuctionDetailsResponse) -> Unit, val auctionId: Int)