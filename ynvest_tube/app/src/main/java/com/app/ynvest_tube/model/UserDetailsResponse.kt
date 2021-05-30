package com.app.ynvest_tube.model

data class UserDetailsResponse(val attendingAuctions: ArrayList<Auction>, val actualRents: ArrayList<Rent>,
                               val expiredRents: ArrayList<Rent>)