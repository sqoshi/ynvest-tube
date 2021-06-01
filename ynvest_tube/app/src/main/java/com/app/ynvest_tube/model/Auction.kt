package com.app.ynvest_tube.model

data class Auction(val id: Int,
                   val video: Video,
                   val starting_price: Int,
                   val last_bid_value: Int?,
                   val user_contribution: Int,
                   val rental_duration: String,
                   val rental_expiration_date: String,
                   val auction_expiration_date: String,
                   val video_views_on_sold: Long?)
